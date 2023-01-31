package recipeo.metrics;

/**
 * Constant values for use with metrics.
 */
public class MetricsConstants {
    public static final String GETRECIPE_RECIPENOTFOUND_COUNT = "GetRecipe.RecipeNotFoundException.Count";

    public static final String GETRECIPESFORUSER_RECIPENOTFOUND_COUNT = "GetRecipesForUser.RecipeNotFoundException.Count";
    public static final String GETCATEGORY_CATEGORYNOTFOUND_COUNT = "GetCategory.CategoryNotFoundException.Count";
    public static final String GETCATEGORIESFORUSER_CATEGORYNOTFOUND_COUNT = "GetCategoriesForUser.CategoryNotFoundException.Count";
    
    public static final String UPDATERECIPE_INVALIDATTRIBUTEVALUE_COUNT =
            "UpdateRecipe.InvalidAttributeValueException.Count";

    public static final String UPDATECATEGORY_INVALIDATTRIBUTEVALUE_COUNT =
            "UpdateCategory.InvalidAttributeValueException.Count";
    
    public static final String UPDATERECIPE_INVALIDATTRIBUTECHANGE_COUNT =
            "UpdateRecipe.InvalidAttributeChangeException.Count";

    public static final String UPDATECATEGORY_INVALIDATTRIBUTECHANGE_COUNT =
            "UpdateCategory.InvalidAttributeChangeException.Count";
    public static final String SERVICE = "Service";
    public static final String SERVICE_NAME = "RecipeoService";
    public static final String NAMESPACE_NAME = "Capstone/RecipeoService";
}
