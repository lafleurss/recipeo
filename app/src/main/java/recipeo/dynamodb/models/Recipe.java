package recipeo.dynamodb.models;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import recipeo.converters.ZonedDateTimeConverter;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a recipe in the recipe table.
 */
@DynamoDBTable(tableName = "recipe")
public class Recipe {

    private String recipeId;
    private String recipeName;
    private String userId;
    private Integer servings;
    private Integer prepTime;
    private Integer cookTime;
    private Integer totalTime;
    private List<String> ingredients;
    private List<String> instructions;
    private String categoryId;
    private String categoryName;
    private Set<String> tags;
    private ZonedDateTime lastAccessed;
    private Boolean isFavorite;

    @DynamoDBHashKey(attributeName = "userId")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @DynamoDBRangeKey(attributeName = "recipeId")
    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    @DynamoDBAttribute(attributeName = "recipeName")
    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    @DynamoDBAttribute(attributeName = "servings")
    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    @DynamoDBAttribute(attributeName = "prepTime")
    public Integer getPrepTime() {
        return prepTime;
    }

    public void setPrepTime(Integer prepTime) {
        this.prepTime = prepTime;
    }

    @DynamoDBAttribute(attributeName = "cookTime")
    public Integer getCookTime() {
        return cookTime;
    }

    public void setCookTime(Integer cookTime) {
        this.cookTime = cookTime;
    }

    @DynamoDBAttribute(attributeName = "totalTime")
    public Integer getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Integer totalTime) {
        this.totalTime = totalTime;
    }

    @DynamoDBAttribute(attributeName = "ingredients")
    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    @DynamoDBAttribute(attributeName = "instructions")
    public List<String> getInstructions() {
        return instructions;
    }

    public void setInstructions(List<String> instructions) {
        this.instructions = instructions;
    }

    @DynamoDBAttribute(attributeName = "categoryId")
    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    @DynamoDBAttribute(attributeName = "categoryName")
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @DynamoDBAttribute(attributeName = "tags")
    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    @DynamoDBAttribute(attributeName = "lastAccessed")
    @DynamoDBTypeConverted(converter = ZonedDateTimeConverter.class)
    public ZonedDateTime getLastAccessed() {
        return lastAccessed;
    }

    public void setLastAccessed(ZonedDateTime lastAccessed) {
        this.lastAccessed = lastAccessed;
    }

    @DynamoDBAttribute(attributeName = "isFavorite")
    public Boolean getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(Boolean favorite) {
        isFavorite = favorite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return recipeId.equals(recipe.recipeId) && recipeName.equals(recipe.recipeName) && userId.equals(recipe.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recipeId, recipeName, userId);
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "recipeId='" + recipeId + '\'' +
                ", recipeName='" + recipeName + '\'' +
                ", userId='" + userId + '\'' +
                ", servings=" + servings +
                ", prepTime=" + prepTime +
                ", cookTime=" + cookTime +
                ", totalTime=" + totalTime +
                ", ingredients=" + ingredients +
                ", instructions=" + instructions +
                ", categoryId='" + categoryId + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", tags=" + tags +
                ", lastAccessed=" + lastAccessed +
                ", isFavorite=" + isFavorite +
                '}';
    }
}



