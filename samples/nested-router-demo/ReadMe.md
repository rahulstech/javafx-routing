## Dive into Nested Routing with JavaFx-Routing: A Comprehensive Guide

**Unleash the Power of Nested Screens**

This demo showcases the art of handling sub-screens within a main screen, a cornerstone of sophisticated application design. Explore how two [`Router`](../../library/src/main/java/rahulstech/jfx/routing/Router.java) instances collaborate to create a seamless navigation experience:

* **Main Router: Guiding the Way**
    - The main router governs the application's primary screens, setting the stage for your user's journey.
* **Sub-Screen Router: Navigating Within**
    - A dedicated router manages the sub-screens of a specific main screen, providing a focused navigation experience within a particular context.
* **Bridging the Gap: RouterContext and Parent Router Reference**
    - The [`RouterContext`](../../library/src/main/java/rahulstech/jfx/routing/RouterContext.java) of the sub-screen router stores a reference to the parent router, enabling smooth communication and navigation between the two levels.
* **Navigating Across Boundaries: From Sub-Screen to Main Screen**
    - Discover how to effortlessly navigate from a sub-screen to a main screen, creating a cohesive and intuitive user experience.
* **Handling Home Sub-Screen Popbacks: A Graceful Exit**
    - Learn the best practices for managing popbacks from the home sub-screen, ensuring a seamless transition back to the main screen.

**Ready to Explore?**

Fire up the sample by executing the following command in your terminal from the root project directory:

```shell
./gradlew :samples:nested-router-demo:run
```

Unlock the potential of nested routing with JavaFx-Routing and create applications that offer a richer and more immersive user experience!
