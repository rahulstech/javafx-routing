### v2.0.0-rc2
**Features:**
1. **BackstackCallback:** Added callback to listen for different backstack operations like push, pop and multiple pop etc.
2. **Parent Router:** Added reference to the parent [Router](/library/src/main/java/rahulstech/jfx/routing/Router.java) inside a **Router**.

### v2.0.0-rc1
**Changes:**
1. `Transaction` now accepts `RouterContext`.
2. `SingleSceneTransaction` is now a subclass of [`BaseGenericOpeationTransaction`](/library/src/main/java/rahulstech/jfx/routing/transaction/BaseGenericOperationTransaction.java) which provides generic methods to handles different types of transactions. Old transaction methods are deprecated and will be removed later.
3. Added differnt `movePoppingUpto` methods in `Router` to perform moving and popping
4. Indexed peeking and popping are deprecated and will be removed in later version.
5. `RouterOptions` now accepts any key-value pairs.
6. Router backstack entry will no more store the animations. It will be removed in later version

**Features:**
1. **Remove History:** Remove a destination from backstack as soon as you navigate from this destination.
2. **Pop on Moving Forward:** Remove one or more destinations from backstack while navigating to a new destination.
3. **Conditional Backstack Popping:** Pop backstack entry if it passes the test.
4. **Pass key-value pair via RouterOptions:** Pass multiple key-value pairs via `RouterOptions`. `key` is a string and `value` is any object or null.

### v1.0.0
**JavaFX Routing** is a versatile library that allows you to manage multiple screens in a single scene, with support for animated transitions and efficient navigation features. It simplifies navigation in JavaFX applications and provides robust lifecycle management for your controllers.

**Features:**
1. **Multiple Screens in a Single Scene**  
   Seamlessly manage multiple screens within a single scene, enabling smooth transitions between views with various animations.

2. **Single Navigation Handler**  
   The `Router` class is solely responsible for handling all navigation between screens, providing a centralized way to control view transitions.

3. **Configuration via XML or Java**  
   Easily manage your router configurations using either an XML file or directly through Java code, offering flexibility for different development preferences.

4. **Custom Animated Transitions**  
   The `RouterAnimation` abstraction allows you to create custom animations using different JavaFX animation libraries. You can also combine multiple animations to create unique transition effects.

5. **Efficient Lifecycle Management**  
   Controllers have well-defined lifecycle callbacks for managing UI and resources efficiently. These methods let you manage view initialization, visibility, and resource cleanup as needed.

6. **Backstack Navigation**  
   The library keeps track of visited destinations using a **Backstack**, allowing for intuitive back navigation.

7. **Data Passing Between Destinations**  
   Pass data and results between destinations easily, simplifying the communication between different screens in your application.

8. **Single-Top Destination**  
   Ensure that only one instance of a specific destination (by its `id`) exists in the backstack at any time, preventing duplicate views from being created.
