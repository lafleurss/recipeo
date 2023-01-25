package recipeo.models;

import recipeo.converters.ZonedDateTimeConverter;
import recipeo.dynamodb.models.Recipe;

import java.util.List;

import static recipeo.utils.CollectionUtils.copyToList;


public class RecipeModel {
    private final ZonedDateTimeConverter converter;
    private final String recipeId;
    private final String recipeName;
    private final String userId;
    private final String userName;
    private final int servings;
    private final int prepTime;
    private final int cookTime;
    private final int totalTime;
    private final  List<String> ingredients;
    private final List<String> instructions;
    private final String categoryName;
    private final List<String> tags;
    private final String lastAccessed;
    private final boolean isFavorite;

    /**
     * A recipe model that can easily be converted to JSON.
     *
     * @param recipe the employee to be converted.
     */
    public RecipeModel(Recipe recipe, String userName, String userId) {
        converter = new ZonedDateTimeConverter();
        this.recipeId = recipe.getRecipeId();
        this.recipeName = recipe.getRecipeName();
        this.userId = userId;
        this.userName = userName;
        this.servings = recipe.getServings();
        this.prepTime = recipe.getPrepTime();
        this.cookTime = recipe.getCookTime();
        this.totalTime = recipe.getTotalTime();
        this.ingredients = recipe.getIngredients();
        this.instructions = recipe.getInstructions();
        this.tags = copyToList(recipe.getTags());
        this.categoryName = recipe.getCategoryName();
        this.lastAccessed = converter.convert(recipe.getLastAccessed());
        this.isFavorite = recipe.getIsFavorite();
    }

    public String getRecipeId() {
        return recipeId;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsrName() {
        return userName;
    }

    public int getServings() {
        return servings;
    }

    public int getPrepTime() {
        return prepTime;
    }

    public int getCookTime() {
        return cookTime;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public List<String> getIngredients() {
        return copyToList(ingredients);
    }

    public List<String> getInstructions() {
        return copyToList(instructions);
    }

    public String getCategoryName() {
        return categoryName;
    }

    public List<String> getTags() {
        return copyToList(tags);
    }

    public String getLastAccessed() {
        return lastAccessed;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getUserName() {
        return userName;
    }


    private static class Builder {
        private String recipeId;
        private String recipeName;
        private String userId;
        private String userName;
        private int servings;
        private int prepTime;
        private int cookTime;
        private int totalTime;
        private List<String> ingredients;
        private List<String> instructions;
        private String categoryName;
        private List<String> tags;
        private String lastAccessed;
        private boolean isFavorite;

        public Builder withRecipeId(String recipeId){
            this.recipeId = recipeId;
            return this;
        }

        public Builder withRecipeName(String recipeName){
            this.recipeName = recipeName;
            return this;
        }

        public Builder withUserName(String userName){
            this.userName = userName;
            return this;
        }

        public Builder withUserId(String userId){
            this.userId = userId;
            return this;
        }

        public Builder withServings(int servings){
            this.servings = servings;
            return this;
        }

        public Builder withPrepTime(int prepTime){
            this.prepTime = prepTime;
            return this;
        }

        public Builder withCookTime(int cookTime){
            this.cookTime = cookTime;
            return this;
        }

        public Builder withTotalTime(int totalTime){
            this.totalTime = totalTime;
            return this;
        }

        public Builder withIngredients(List<String> ingredients){
            this.ingredients = copyToList(ingredients);
            return this;
        }

        public Builder withInstructions(List<String> ingredients){
            this.ingredients = copyToList(ingredients);
            return this;
        }

        public Builder withCategoryName(String categoryName){
            this.categoryName = categoryName;
            return this;
        }

        public Builder withLastAccessed(String lastAccessed){
            this.lastAccessed = lastAccessed;
            return this;
        }

        public Builder withIsFavorite(boolean isFavorite){
            this.isFavorite = isFavorite;
            return this;
        }
    }
}
