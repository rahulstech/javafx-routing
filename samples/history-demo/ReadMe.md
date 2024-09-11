# Remove Screens on Navigation - Automatically Or When Needed

---

In this demo, we'll explore how to manage navigation history in your JavaFX application. We'll cover two key scenarios:
* **Destinations with no history:** These destinations are removed from the backstack immediately after navigation, preventing users from going back to them.
* **Popping when moving forward:** This technique is useful for multistep forms where you want to clear the history when the user reaches the final step.


**Ready to Explore?**

Fire up the sample by executing the following command in your terminal from the root project directory:

```bash
./gradlew :samples:history-demo:run
```