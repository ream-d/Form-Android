package ream.form.binder;

/**
 * 2017/1/13 0013.
 */
public interface IReamForm<T> {
    void bind(T target);

    void resetForm();

    void storeForm();

    String formName();
}
