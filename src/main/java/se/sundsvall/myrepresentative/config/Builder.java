package se.sundsvall.myrepresentative.config;

/**
 * Meta-annotation around the Jilt {@code Builder} annotation to avoid having
 * to repeat the same attributes over and over again.
 */
@org.jilt.Builder(factoryMethod = "create", setterPrefix = "with", toBuilder = "from")
public @interface Builder {
}
