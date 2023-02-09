package recipeo.dependency;

import dagger.Component;

import recipeo.activity.CreateCategoryActivity;
import recipeo.activity.CreateRecipeActivity;
import recipeo.activity.GetCategoriesForUserActivity;
import recipeo.activity.GetCategoryActivity;
import recipeo.activity.GetRecipeActivity;
import recipeo.activity.GetRecipesForUserActivity;
import recipeo.activity.GetRecipesForUserInCategoryActivity;
import recipeo.activity.SearchRecipesActivity;
import recipeo.activity.UpdateCategoryActivity;
import recipeo.activity.UpdateRecipeActivity;

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
     * @return GetRecipesForUserInCategoryRequest
     */
    GetRecipesForUserInCategoryActivity provideGetRecipesForUserInCategoryActivity();

    /**
     * Provides the relevant activity.
     * @return CreateCategoryActivity
     */
    CreateRecipeActivity provideCreateRecipeActivity();

    /**
     * Provides the relevant activity.
     * @return UpdateRecipeActivity
     */
    UpdateRecipeActivity provideUpdateRecipeActivity();


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

    /**
     * Provides the relevant activity.
     * @return SearchRecipesActivity
     */
    SearchRecipesActivity provideSearchPlaylistsActivity();

}
