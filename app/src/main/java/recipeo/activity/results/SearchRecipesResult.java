package recipeo.activity.results;

import recipeo.models.RecipeModel;

import java.util.ArrayList;
import java.util.List;

public class SearchRecipesResult {
    private final List<RecipeModel> recipes;

    private SearchRecipesResult(List<RecipeModel> recipes) {
        this.recipes = recipes;
    }

    public List<RecipeModel> getRecipes() {
        return recipes;
    }

    @Override
    public String toString() {
        return "SearchPlaylistsResult{" +
                "recipes=" + recipes +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<RecipeModel> recipes ;

        public Builder withRecipes(List<RecipeModel> recipes) {
            this.recipes = new ArrayList<>(recipes);
            return this;
        }

        public SearchRecipesResult build() {
            return new SearchRecipesResult(recipes);
        }
    }
}
