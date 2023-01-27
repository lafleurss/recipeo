package recipeo.models;

import java.util.List;
import java.util.Objects;

import static recipeo.utils.CollectionUtils.copyToList;


public class RecipeModel {
    private final String recipeId;
    private final String recipeName;
    private final String userId;
    private final int servings;
    private final int prepTime;
    private final int cookTime;
    private final int totalTime;
    private final  List<String> ingredients;
    private final List<String> instructions;
    private final String categoryName;
    private final List<String> tags;
    private final String lastAccessed;
    private final String isFavorite;

    private RecipeModel(String recipeId, String recipeName, String userId, int servings, int prepTime, int cookTime, int totalTime, List<String> ingredients, List<String> instructions, String categoryName, List<String> tags, String lastAccessed, String isFavorite) {
        this.recipeId = recipeId;
        this.recipeName = recipeName;
        this.userId = userId;
        this.servings = servings;
        this.prepTime = prepTime;
        this.cookTime = cookTime;
        this.totalTime = totalTime;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.categoryName = categoryName;
        this.tags = tags;
        this.lastAccessed = lastAccessed;
        this.isFavorite = isFavorite;
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

    public String getIsFavorite() {
        return isFavorite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeModel that = (RecipeModel) o;
        return servings == that.servings && prepTime == that.prepTime && cookTime == that.cookTime && totalTime == that.totalTime && recipeId.equals(that.recipeId) && recipeName.equals(that.recipeName) && userId.equals(that.userId) && Objects.equals(ingredients, that.ingredients) && Objects.equals(instructions, that.instructions) && Objects.equals(categoryName, that.categoryName) && Objects.equals(tags, that.tags) && Objects.equals(lastAccessed, that.lastAccessed) && Objects.equals(isFavorite, that.isFavorite);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recipeId, recipeName, userId, servings, prepTime, cookTime, totalTime, ingredients, instructions, categoryName, tags, lastAccessed, isFavorite);
    }

    @Override
    public String toString() {
        return "RecipeModel{" +
                "recipeId='" + recipeId + '\'' +
                ", recipeName='" + recipeName + '\'' +
                ", userId='" + userId + '\'' +
                ", servings=" + servings +
                ", prepTime=" + prepTime +
                ", cookTime=" + cookTime +
                ", totalTime=" + totalTime +
                ", ingredients=" + ingredients +
                ", instructions=" + instructions +
                ", categoryName='" + categoryName + '\'' +
                ", tags=" + tags +
                ", lastAccessed='" + lastAccessed + '\'' +
                ", isFavorite='" + isFavorite + '\'' +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }


    public static class Builder {
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
        private String isFavorite;

        public Builder withRecipeId(String recipeId){
            this.recipeId = recipeId;
            return this;
        }

        public Builder withRecipeName(String recipeName){
            this.recipeName = recipeName;
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

        public Builder withInstructions(List<String> instructions){
            this.instructions = copyToList(instructions);
            return this;
        }

        public Builder withCategoryName(String categoryName){
            this.categoryName = categoryName;
            return this;
        }

        public Builder withTags(List<String> tags){
            this.tags = tags;
            return this;
        }

        public Builder withLastAccessed(String lastAccessed){
            this.lastAccessed = lastAccessed;
            return this;
        }

        public Builder withIsFavorite(String isFavorite){
            this.isFavorite = isFavorite;
            return this;
        }
        public RecipeModel build() {
            return new RecipeModel(recipeId, recipeName, userId, servings, prepTime, cookTime, totalTime, ingredients, instructions, categoryName, tags, lastAccessed, isFavorite );
        }
    }
}
