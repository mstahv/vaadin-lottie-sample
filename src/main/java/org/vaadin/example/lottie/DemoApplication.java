package org.vaadin.example.lottie;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import org.jsoup.nodes.Element;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@SpringBootApplication
@Push
public class DemoApplication implements AppShellConfigurator {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}


	@Component
	public static class ApplicationServiceInitListener
			implements VaadinServiceInitListener {

		@Override
		public void serviceInit(ServiceInitEvent event) {
			event.addIndexHtmlRequestListener(response -> {
				Element head = response.getDocument().head();
				injectSeoAndSocialTags(head,
						"Vaadin Lottie Demo application",
						"Embed Lottie animations into your Vaadin Java application easily.",
						"https://lottie-vaadin-demo.fly.dev/",
						"https://lottie-vaadin-demo.fly.dev/lottie_social_preview.png",
						LocalDate.now());
			});
		}
	}

	/**
	 * Generates and injects Microdata, JSON-LD and Open Graph metadata into the head element of the HTML document.
	 *
	 * @param head          the head element of the HTML document
	 * @param name          the name of the page
	 *                      (e.g. "MyApp")
	 * @param description   the description of the page
	 *                      (e.g. "This is my application.")
	 * @param url           the URL of the page
	 *                      (e.g. "http://localhost:8080/")
	 * @param imageUrl      the URL of the image to be used as a preview
	 *                      (e.g. "http://localhost:8080/images/socialpreview.png")
	 * @param datePublished the date when the page was published
	 *                      (e.g. LocalDate.now())
	 */
	public static void injectSeoAndSocialTags(Element head, String name, String description, String url,
											  String imageUrl, LocalDate datePublished) {
		String formattedDate = datePublished != null
				? datePublished.format(DateTimeFormatter.ISO_DATE)
				: LocalDate.now().format(DateTimeFormatter.ISO_DATE);

		// JSON-LD
		String jsonLdContent = String.format("""
                {
                  "@context": "https://schema.org",
                  "@type": "WebPage",
                  "name": "%s",
                  "description": "%s",
                  "url": "%s",
                  "datePublished": "%s",
                  "image": {
                    "@type": "ImageObject",
                    "contentUrl": "%s",
                    "caption": "%s"
                  }
                }
                """, name, description, url, formattedDate, imageUrl, name);

		head.appendElement("script")
				.attr("type", "application/ld+json")
				.appendText(jsonLdContent);

		// Microdata
		head.appendElement("meta").attr("itemprop", "name")
				.attr("content", name);
		head.appendElement("meta").attr("itemprop", "description")
				.attr("content", description);
		head.appendElement("meta").attr("itemprop", "url")
				.attr("content", url);
		head.appendElement("meta").attr("itemprop", "datePublished")
				.attr("content", formattedDate);
		head.appendElement("meta").attr("itemprop", "image")
				.attr("content", imageUrl);

		// Open Graph
		head.appendElement("meta").attr("property", "og:title")
				.attr("content", name);
		head.appendElement("meta").attr("property", "og:description")
				.attr("content", description);
		head.appendElement("meta").attr("property", "og:url")
				.attr("content", url);
		head.appendElement("meta").attr("property", "og:image")
				.attr("content", imageUrl);
		head.appendElement("meta").attr("property", "og:type")
				.attr("content", "website");
		head.appendElement("meta").attr("property", "og:site_name")
				.attr("content", name);
	}
}
