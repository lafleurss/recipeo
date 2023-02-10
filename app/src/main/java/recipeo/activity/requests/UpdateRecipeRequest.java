package recipeo.activity.requests;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.ArrayList;
import java.util.List;

import static recipeo.utils.CollectionUtils.copyToList;

@JsonDeserialize(builder = UpdateRecipeRequest.Builder.class)
public class UpdateRecipeRequest {
    private final String recipeIdFromPath;
    private final String recipeName;
    private final String userId;
    private final Integer servings;
    private final Integer prepTime;
    private final Integer cookTime;
    private final Integer totalTime;
    private final List<String> ingredients;
    private final List<String> instructions;
    private final String categoryName;
    private final List<String> tags;
    private final String isFavorite;

    private UpdateRecipeRequest(String recipeIdFromPath, String recipeName, String userId, Integer servings,
                                Integer prepTime, Integer cookTime, Integer totalTime, List<String> ingredients,
                                List<String> instructions, String categoryName, List<String> tags,
                                String isFavorite) {
        this.recipeIdFromPath = recipeIdFromPath;
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
        this.isFavorite = isFavorite;
    }

    public String getRecipeId() {
        return recipeIdFromPath;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public String getUserId() {
        return userId;
    }

    public Integer getServings() {
        return servings;
    }

    public Integer getPrepTime() {
        return prepTime;
    }

    public Integer getCookTime() {
        return cookTime;
    }

    public Integer getTotalTime() {
        return totalTime;
    }

    public List<String> getIngredients() {
        return new ArrayList<>(ingredients);
    }

    public List<String> getInstructions() {
        return new ArrayList<>(instructions);
    }

    public String getCategoryName() {
        return categoryName;
    }

    public List<String> getTags() {
        return copyToList(tags);
    }

    public String getIsFavorite() {
        return isFavorite;
    }

    @Override
    public String toString() {
        return "UpdateRecipeRequest{" +
                "recipeIdFromPath='" + recipeIdFromPath + '\'' +
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
                ", isFavorite='" + isFavorite + '\'' +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder
    public static class Builder {
        private String recipeIdFromPath;
        private String recipeName;
        private String userId;
        private Integer servings;
        private Integer prepTime;
        private Integer cookTime;
        private Integer totalTime;
        private List<String> ingredients;
        private List<String> instructions;
        private String categoryName;
        private List<String> tags;
        private String isFavorite;

        public Builder withRecipeId(String recipeId) {
            this.recipeIdFromPath = recipeId;
            return this;
        }
        public Builder withRecipeName(String recipeName) {
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
            this.tags = copyToList(tags);
            return this;
        }

        public Builder withIsFavorite(String isFavorite) {
            this.isFavorite = isFavorite;
            return this;
        }
        public UpdateRecipeRequest build() {
            return new UpdateRecipeRequest(recipeIdFromPath, recipeName, userId, servings, prepTime,
                    cookTime, totalTime, ingredients, instructions, categoryName,
                    tags, isFavorite);
        }

    }
}
