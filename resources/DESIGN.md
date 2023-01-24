# Recipeo
Introducing the ultimate personal recipe curation app - "Recipeo"! This app allows you to easily add, edit, and organize your favorite recipes, whether it's a recipe passed down from your grandma or something you found online. With a user-friendly interface, you can easily search through your recipes through their tags, edit the recipe to include your own recipe enhancements or delete recipes that you no longer need (... because you're now a vegan!).

You can also organize your recipes by categories like Recently Used, Favorites, or your own custom Category  to organize your recipes. Say goodbye to cluttered recipe cards or lost recipe tweaks and hello to a streamlined, organized recipe collection with Recipeo.

# Business Requirements

## User Stories
1. As a user I want to be able to login to the app using Amazon Cognito
2. As a user I want to Create a Recipe card so that I can save it
3. As a user I want to optionally Assign a Category to a Recipe when Creating a Recipe
4. As a user I want to Edit a Recipe card so that I can note my recipe adjustments
5. As a user I want to Delete a Recipe card because I don't need it anymore
6. As a user I want to Search for recipes via tags
7. As a user I want to be able to see top 25 recipes in my Recently Used Category
8. As a user I want to be able to see all recipes in my Favorites Category
9. As a user I want to be able to create my own custom Categories 
10. As a user I want to be able to sort my recipes by recipe name A-Z, Z-A
11. As a user I want to be able to delete my custom Category without deleting the recipes in it


A list of “stretch goals”/features
1. As a user I want to be able to selectively add/remove recipes to my custom Category
2. As a user I want to be able to use JustTheRecipe api in this app to extract a recipe from any URL
2. Ability to store and display a picture for every recipe
3. Ability to invite friends, add friends and share recipes within the app
4. Create grocery lists within the app


# UML class diagram
[UML Class Diagram](images/RecipeoUMLClass.png)


# DynamoDB 
List of Tables and global secondary indexes:

### Recipe
- recipeId:String (partition key)
- recipeName:String (sort key)
- userId:String 
- servings:Integer
- prepTime:Integer
- cookTime:Integer
- totalTime:Integer
- ingredients:List<String>
- instructions:List<String>
- category:String
- tags:Set<String>
- lastAccessed:String
- isFavourite:Boolean

### Category
- categoryId:String (partition key)
- categoryName:String
- userId:String (partition key)

### UserRecipe (GSI)
- userId:String (partition key)
- recipeId:String

### UserCategory (GSI)
- userId:String (partition key)
- categoryId:String


# API Endpoints
## Get Recipes for User Endpoint
* Accepts GET requests to `/recipes/:userId`
* Accepts a userId and returns a list of RecipeModels created by that customer.
* If the given user has not created any recipes, an empty list will be returned

## Get Categories for User Endpoint
* Accepts GET requests to `/categories/:userId`
* Accepts a userId and returns a list of CategoryModels created by that customer.
* If the given user has not created any categories, an empty list will be returned

## Get Recipe Endpoint
* Accepts GET requests to `/recipes/:recipeId`
* Accepts a recipe ID and returns the corresponding RecipeModel.
  * If the given recipe ID is not found, will throw a RecipeNotFoundException

## Get Category Endpoint
* Accepts GET requests to `/categories/:categoryId`
* Accepts a category ID and returns the corresponding CategoryModel.
  * If the given recipe ID is not found, will throw a CategoryNotFoundException

## Create Recipe Endpoint
* Accepts POST requests to `/recipes`
* Accepts input to create a new recipe:
``{"recipeName":"name", "userId":"id", "servings":4, "prepTime":1200000000, "cookTime": 1200000000, "totalTime":2400000000, "ingredients": [
  {
  "name": "1 cup chickpeas (dry)"
  },
  {
  "name": "1  large onion (finely chopped)"
  },    
  {
  "name": "Cilantro for garnish (optional)"
  }
  ], "instructions": [
  {
  "name": "do x"
  },
  {
  "name": "prep y"
  },    
  {
  "name": "another step"
  }
  ], "tags": [
        "tag1",
        "tag2",
        "tag3"
      ],
  "isFavorite" : false,
  "category": "Uncategorized" } ``
* Optional: list of tags (default: no tags), (default: false), category (default: Uncategorized). Returns the new recipe with the lastAccessed timestamp set to time of POST, including a unique recipe ID assigned by the Recipeo service.

  * For security concerns, we will validate the provided recipe name does not contain any invalid characters: " ' \`
  * If the recipe name contains any of the invalid characters, will throw an InvalidAttributeValueException.

## Create Category Endpoint
* Accepts POST requests to `/categories`
* Accepts input to create a new category: `{"categoryName" : "name"}`. Returns the new category, including a unique category ID assigned by the Recipeo service.
  * For security concerns, we will validate the provided recipe name does not contain any invalid characters: " ' \`
  * If the recipe name contains any of the invalid characters, will throw an InvalidAttributeValueException.

## Update Recipe Endpoint
* Accepts PUT requests to `/recipes/:recipeId`
* Accepts input to update a recipe `{"recipeName":"name",  "servings":4, "prepTime":1200000000, "cookTime": 1200000000, "totalTime":2400000000, "ingredients": [ { "name": "1 cup chickpeas (dry)" }, { "name": "1 large onion (finely chopped)" }, { "name": "Cilantro for garnish (optional)" } ], "instructions": [ { "name": "do x" }, { "name": "prep y" }, { "name": "another step" } ], "tags": [ "tag1", "tag2", "tag3" ], "isFavorite" : false, "category": "Uncategorized" }`. Returns the updated recipe.
  * If the recipe ID is not found, will throw a RecipeNotFoundException
  * For security concerns, we will validate the provided recipe name does not contain invalid characters: " ' \`
  * If the recipe name contains invalid characters, will throw an InvalidAttributeValueException

## Update Category Endpoint
* Accepts PUT requests to `/categories/:categoryId`
* Accepts input to update a category `{"categoryName" : "name"}`.  Returns the updated CategoryModel.
  * If the category ID is not found, will throw a CategoryNotFoundException
  * For security concerns, we will validate the provided recipe name does not contain invalid characters: " ' \`
  * If the recipe name contains invalid characters, will throw an InvalidAttributeValueException

## Search Recipes Endpoint
* Accepts GET requests to `/recipes/search`
* Accepts parameters `recipeName` or `tags` to search for recipes  using recipe names or tags. Returns a list of RecipeModel.
  * If the recipe ID is not found, will throw a RecipeNotFoundException
  * For security concerns, we will validate the provided recipe name does not contain invalid characters: " ' \`
  * If the recipe name contains invalid characters, will throw an InvalidAttributeValueException


# UML Sequence Diagram
[Create Recipe](images/CreateRecipe.png)


# Mockups
Mockups (aka “wireframes”) of the front-end web application
#### [Home](images/home.png)

![Home](images/home.png)

#### [Recipe](images/recipe.png)

![Recipe](images/recipe.png)

#### [Category](images/category.png)

![Category](images/category.png)


# AWS Services
* CloudFormation
* S3
* CloudFront
* API Gateway
* Lambda
* Dynamo DB
