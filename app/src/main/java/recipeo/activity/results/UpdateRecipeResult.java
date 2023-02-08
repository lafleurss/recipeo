package recipeo.activity.results;

import recipeo.models.RecipeModel;

public class UpdateRecipeResult {
    private final RecipeModel recipe;

    private UpdateRecipeResult(RecipeModel recipe) {
        this.recipe = recipe;
    }

    public RecipeModel getRecipe() {
        return recipe;
    }

    @Override
    public String toString() {
        return "UpdateRecipeResult{" +
                "recipe=" + recipe +
                '}';
    }


    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private RecipeModel recipe;

        public Builder withRecipe(RecipeModel recipe) {
            this.recipe = recipe;
            return this;
        }

        public UpdateRecipeResult build() {
            return new UpdateRecipeResult(recipe);
        }
    }
}
