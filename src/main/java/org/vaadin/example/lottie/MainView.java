package org.vaadin.example.lottie;

import com.vaadin.flow.component.ClickEvent;


import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.Lumo;
import com.vaadin.flow.theme.lumo.LumoUtility;

@Route()
public class MainView extends VerticalLayout {

    private LottieComponent saveStatus = new LottieComponent(
            "/success.lottie", false, false, "3rem", "3rem");
    private LottieComponent liveStatus = new LottieComponent(
            "/live.lottie", true, true, "4rem", "2rem");
    private LottieComponent premiumStatus = new LottieComponent(
            "/premium.lottie", true, true, "4rem", "2rem");
    private LottieComponent confetti;

    public MainView() {
        add(new H1("Welcome to Vaadin Lottie sample"));

        // Just a sample form to collect user details
        add(new SampleForm());

        Button submitButton = new Button("Save", event -> {
            // Play the save status animation
            saveStatus.play();
        });
        add(new HorizontalLayout(submitButton, saveStatus));

        // Add a button to render confetti animation
        Button confettiBtn = new Button("Confetti!", this::confetti);
        confettiBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        // Go live button
        Button liveBtn = new Button("Go live!", this::toggleLive);
        liveBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        // Misc configuration
        liveStatus.setVisible(false);

        add(new HorizontalLayout(confettiBtn, liveBtn));
    }

    private void toggleLive(ClickEvent<Button> buttonClickEvent) {
        if (liveStatus.isVisible()) {
            liveStatus.setVisible(false);
            remove(liveStatus);
            buttonClickEvent.getSource().setText("Go Live!");
        } else {
            addComponentAsFirst(liveStatus);
            liveStatus.setVisible(true);
            buttonClickEvent.getSource().setText("Go Offline!");
        }
    }

    /** Dynamically render confetti in the UI.
     */
    public void confetti(ClickEvent<Button> buttonClickEvent) {

        // Remove the existing confetti if any
        if (this.confetti != null) {
            UI.getCurrent().remove(this.confetti);
        }

        // Add the full screen confetti animation
        confetti = new LottieComponent("/confetti.lottie",
                true, false, null, null);
        UI.getCurrent().add(confetti);
        confetti.makeFullOverlay(); // Make the confetti full overlay
    }

    /** Go premium.
     */
    private void activatePremium(ClickEvent<LottieComponent> lottieComponentClickEvent) {
        Notification.show("Premium Subscription needs to be activated for this feature to be available. ");
    }



    /** Sample form to collect user details.
     */
    public class SampleForm extends FormLayout {

        TextField firstName = new TextField("First Name");
        TextField lastName = new TextField("Last Name");
        EmailField email = new EmailField("Email");
        NumberField phone = new NumberField("Phone Number");
        DatePicker birthDate = new DatePicker("Birth Date");

        public SampleForm() {
            add(firstName, lastName, email, phone, new HorizontalLayout(birthDate, premiumStatus));
            premiumStatus.makeRelative("0.4rem", "-8.2rem");
            premiumStatus.addClickListener(MainView.this::activatePremium);

        }
    }


}
