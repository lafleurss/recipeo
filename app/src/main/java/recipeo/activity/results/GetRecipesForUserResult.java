package recipeo.activity.results;

import recipeo.models.RecipeModel;

import java.util.List;

public class GetRecipesForUserResult {
    private final List<RecipeModel> recipes;

    public GetRecipesForUserResult(List<RecipeModel> recipes) {
        this.recipes = recipes;
    }

    public List<RecipeModel> getRecipes() {
        return List.copyOf(recipes);
    }

    @Override
    public String toString() {
        return "GetRecipesForUserResult{" +
                "recipes=" + recipes +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<RecipeModel> recipeList;

        public Builder withRecipes(List<RecipeModel> recipeList) {
            this.recipeList = List.copyOf(recipeList);
            return this;
        }

        public GetRecipesForUserResult build(){ return new GetRecipesForUserResult(recipeList);}
    }
}
