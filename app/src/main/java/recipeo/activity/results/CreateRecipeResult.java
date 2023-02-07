package recipeo.activity.results;

import recipeo.models.RecipeModel;

public class CreateRecipeResult {

    private final RecipeModel recipe;

    private CreateRecipeResult(RecipeModel recipe) {
        this.recipe = recipe;
    }

    public RecipeModel getRecipe() {
        return recipe;
    }

    @Override
    public String toString() {
        return "CreateRecipeResult{" +
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

        public CreateRecipeResult build() {
            return new CreateRecipeResult(recipe);
        }
    }
}
