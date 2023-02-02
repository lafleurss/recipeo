package recipeo.dependency;

import dagger.Component;

import recipeo.activity.CreateCategoryActivity;
import recipeo.activity.GetCategoriesForUserActivity;
import recipeo.activity.GetCategoryActivity;
import recipeo.activity.GetRecipeActivity;
import recipeo.activity.GetRecipesForUserActivity;
import recipeo.activity.UpdateCategoryActivity;

import javax.inject.Singleton;

/**
 * Dagger component for providing dependency injection in the Recipeo Service.
 */

@Singleton
@Component(modules = {DaoModule.class, MetricsModule.class})
public interface ServiceComponent {
    /**
     * Provides the relevant activity.
     * @return GetRecipeActivity
     */
    GetRecipeActivity provideGetRecipeActivity();

    /**
     * Provides the relevant activity.
     * @return GetRecipesForUserActivity
     */
    GetRecipesForUserActivity provideGetRecipesForUserActivity();

    /**
     * Provides the relevant activity.
     * @return GetCategoryActivity
     */
    GetCategoryActivity provideGetCategoryActivity();

    /**
     * Provides the relevant activity.
     * @return GetCategoresForUserActivity
     */
    GetCategoriesForUserActivity provideGetCategoriesForUserActivity();

    /**
     * Provides the relevant activity.
     * @return CreateCategoryActivity
     */
    CreateCategoryActivity provideCreateCategoryActivity();


    /**
     * Provides the relevant activity.
     * @return UpdateCategoryActivity
     */
    UpdateCategoryActivity provideUpdateCategoryActivity();

}
