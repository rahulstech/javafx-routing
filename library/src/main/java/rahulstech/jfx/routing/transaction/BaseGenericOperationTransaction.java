package rahulstech.jfx.routing.transaction;

import rahulstech.jfx.routing.RouterContext;
import rahulstech.jfx.routing.RouterOptions;
import rahulstech.jfx.routing.Transaction;

import java.util.function.Supplier;

/**
 * The {@code BaseGenericOperationTransaction} is an abstract class that
 * extends {@link Transaction}. This class serves as the foundation for
 * implementing generic routing operations related to UI transactions,
 * such as showing, hiding, and popping screens, while providing access
 * to the {@link RouterContext}. It supports customizable routing operations
 * for the specified {@code TargetType}, which must extend {@link Transaction.Target}.
 *
 * @param <TargetType> The type of target that this transaction operates on,
 *                     which must extend {@link Transaction.Target}.
 * @author Rahul Bagchi
 * @since 2.0
 */
public abstract class BaseGenericOperationTransaction<TargetType extends Transaction.Target> extends Transaction {

    /**
     * Constructs a new {@code BaseGenericOperationTransaction} instance
     * with the provided {@link RouterContext}.
     *
     * @param context non-null {@code RouterContext} instance
     * @throws NullPointerException if {@code RouterContext} is {@code null}
     * @see #setRouterContext(RouterContext)
     */
    public BaseGenericOperationTransaction(RouterContext context) {
        super();
        setRouterContext(context);
    }

    /************************************************************
     *                 Operation Related Methods                *
     ************************************************************/

    /**
     * Displays the specified {@code target}. Use this method to show the target for the first time and
     * push to backstack. Optionally pass extra configurations using {@link RouterOptions}.
     *
     * @param target  non-null subclass of {@link rahulstech.jfx.routing.Transaction.Target} to display.
     * @param options nullable {@code RouterOptions} for extra configuration .
     */
    public abstract void show(TargetType target, RouterOptions options);

    /**
     * Displays the single-top target, ensuring that there is
     * at most one instance of the target with the specified {@code tag} in the backstack.
     * If such a target does not exist, it is created using the provided {@code supplier}.
     * Optionally pass extra configurations using {@link RouterOptions}
     *
     * @param tag      the unique tag identifying the target.
     * @param supplier a non-null {@link Supplier} that provides the target instance if it does not exist.
     * @param options  nullable {@code RouterOptions} for extra configuration
     */
    public abstract void showSingleTop(String tag, Supplier<TargetType> supplier, RouterOptions options);

    /**
     * Shows the target with {@code tag} if and only if it exists in backstack.
     * Optionally pass extra configurations using {@link RouterOptions}
     *
     * @param tag     the unique tag identifying the target to display.
     * @param options nullable {@code RouterOptions} for extra configuration
     */
    public abstract void popShow(String tag, RouterOptions options);

    /**
     * Hides the target with the specified {@code tag}. Optionally pass extra configurations using {@link RouterOptions}.
     *
     * @param tag     the unique tag identifying the target to hide.
     * @param options nullable {@code RouterOptions} for extra configuration
     */
    public abstract void hide(String tag, RouterOptions options);

    /**
     * Hides and destroy the target with {@code tag} if and only if it exists in backstack.
     * Optionally pass extra configurations using {@link RouterOptions}
     * 
     * @param tag     the unique tag identifying the target to hide.
     * @param options nullable {@code RouterOptions} for extra configuration
     */
    public abstract void popHide(String tag, RouterOptions options);
}
