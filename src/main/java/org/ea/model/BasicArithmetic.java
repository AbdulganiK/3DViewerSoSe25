package org.ea.model;

/**
 * An interface for basic arithmetic operations on generic types.
 *
 * @param <T> the type on which arithmetic operations are performed
 */
public interface BasicArithmetic<T> {

        /**
         * Adds the specified value to this value.
         *
         * @param other the value to add
         * @return the result of the addition
         * @precondition {@code other} must not be {@code null}
         * @postcondition Returns a new value representing the sum of {@code this} and {@code other}
         */
        T add(T other);

        /**
         * Subtracts the specified value from this value.
         *
         * @param other the value to subtract
         * @return the result of the subtraction
         * @precondition {@code other} must not be {@code null}
         * @postcondition Returns a new value representing the difference of {@code this} minus {@code other}
         */
        T subtract(T other);

        /**
         * Multiplies this value by the specified value.
         *
         * @param other the value to multiply by
         * @return the result of the multiplication
         * @precondition {@code other} must not be {@code null}
         * @postcondition Returns a new value representing the product of {@code this} and {@code other}
         */
        T multiply(T other);
}