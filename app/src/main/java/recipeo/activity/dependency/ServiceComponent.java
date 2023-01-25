package recipeo.activity.dependency;

import dagger.Component;
import dagger.Provides;
import recipeo.activity.GetCategoryActivity;
import recipeo.activity.GetRecipeActivity;

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
     * @return GetCategoryActivity
     */
    GetCategoryActivity provideGetCategoryActivity();



}
