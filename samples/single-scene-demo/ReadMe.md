## Unleash the Power of JavaFx-Routing: A Deep Dive into Advanced Features ## 

---

This advanced demo unlocks the full potential of JavaFx-Routing, empowering you to craft dynamic and user-friendly single-scene applications. Dive into the heart of these capabilities and discover how to:

* **Effortlessly Design FXML Layouts with RouterStackPane:**
    - Master the declaration of `RouterStackPane` within FXML, seamlessly integrating its properties for a streamlined development workflow.
* **Navigate with Precision: Single Top Destinations and Beyond**
    - Implement the `singleTop` attribute in your XML configurations to ensure only one instance of a destination resides in the backstack at any given time, maintaining a clear navigation history.
* **Pass Results with Confidence:**
    - Equip your destinations with the ability to produce results, effectively passing them back to the caller for further processing. This fosters a more interactive and data-driven user experience.
* **Pop the Backstack with Ease: Multiple Backstack Entry Removal**
    - Leverage the `popMultiple` feature to effortlessly remove multiple entries from the backstack, reaching a specific destination in a single swipe. Ideal for multistep forms where you want to return to the starting point upon completion.
* **Embrace Flexibility: Layouts Beyond FXML**
    - Witness the power of creating layouts dynamically during the lifecycle's `create` method. This approach grants you greater flexibility for scenarios requiring custom logic or on-the-fly adjustments.
* **Craft Bespoke Animations: Customizing Transitions**
    - Delve into the creation of custom animations, extending the [`RouterAnimation`](../../library/src/main/java/rahulstech/jfx/routing/element/RouterAnimation.java) class to tailor transition effects that perfectly suit your application's aesthetics. Explore the provided examples of `scale_up_xy_slide_in_bottom` and `scale_down_xy_slide_out_bottom` for inspiration.
* **Harness the Power of Arguments: Setting and Passing Data**
    - Learn how to effectively set `arguments` within the router configuration XML, allowing you to transmit essential data to destinations on navigation. This sample showcases how to add home data for the home destination, demonstrating its practical application.
* **Handle Navigation Errors Gracefully:**
    - Equip yourself with the tools to handle navigation exceptions, ensuring robustness in your application. These exceptions might arise due to non-existent destinations, missing required data, or other unexpected scenarios. Refer to the [`ScreenController`](./src/main/java/rahulstech/jfx/singlescenedemo/ScreenController.java) class for guidance on error handling practices.
* **Fine-Tune Navigation Transitions: Customizing in Java**
    - Discover how to personalize navigation transition animations using Java and the [`RouterOptions`](../../library/src/main/java/rahulstech/jfx/routing/RouterOptions.java) class. Take control of the look and feel of your application's navigation flow.
* **Dynamic Stage Titles: Reflecting Navigation Status**
    - Learn how to update the stage title in response to destination changes. Leverage the `title` attribute of destinations to set the desired title while navigating. If no title is specified, the destination's `id` is used for clarity.
  
**Ready to Explore?**

Fire up the sample by executing the following command in your terminal from the root project directory:

```shell
./gradlew :samples:single-scene-demo:run
```

Embrace the world of JavaFx-Routing and create exceptional single-scene applications that are both visually captivating and functionally superior!