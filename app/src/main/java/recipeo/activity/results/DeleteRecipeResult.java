package recipeo.activity.results;

import recipeo.models.RecipeModel;

public class DeleteRecipeResult {
    private final RecipeModel recipe;

    private DeleteRecipeResult(RecipeModel recipe) {
        this.recipe = recipe;
    }

    public RecipeModel getRecipe() {
        return recipe;
    }

    @Override
    public String toString() {
        return "DeleteRecipeResult{" +
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

        public DeleteRecipeResult build() {
            return new DeleteRecipeResult(recipe);
        }
    }
}
