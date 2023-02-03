package recipeo.activity.results;

import recipeo.models.RecipeModel;

import java.util.List;

public class GetRecipesForUserInCategoryResult {
    private final List<RecipeModel> recipes;

    private GetRecipesForUserInCategoryResult(List<RecipeModel> recipes) {
        this.recipes = recipes;
    }

    public List<RecipeModel> getRecipes() {
        return List.copyOf(recipes);
    }

    @Override
    public String toString() {
        return "GetRecipesForUserInCategoryResult{" +
                "recipes=" + recipes +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<RecipeModel> recipeList;

        public Builder withRecipes(List<RecipeModel> recipeList) {
            this.recipeList = List.copyOf(recipeList);
            return this;
        }

        public GetRecipesForUserInCategoryResult build() {
            return new GetRecipesForUserInCategoryResult(recipeList);
        }
    }
}
