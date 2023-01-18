# Recipeo
Introducing the ultimate personal recipe curation app - "Recipeo"! This app allows you to easily add, edit, and organize your favorite recipes, whether it's a recipe passed down from your grandma or something you found online. With a user-friendly interface, you can easily search through your recipes through their tags, edit the recipe to include your own recipe enhancements or delete recipes that you no longer need (... because you're now a vegan!).

You can also organize your recipes by categories like Recently Used, Favorites, or your own custom Category  to organize your recipes. Say goodbye to cluttered recipe cards or lost recipe tweaks and hello to a streamlined, organized recipe collection with Recipeo.

# Business Requirements

## User Stories
A list of business requirements written in a user story format - you must have at least 9 user stories (see additional note below on scope)
1. As a user I want to be able to login to the app using Amazon Cognito
2. As a user I want to Add a Recipe card so that I can save it - POST API
3. As a user I want to Edit a Recipe card so that I can note my recipe adjustments - PUT API
4. As a user I want to Delete a Recipe card because I don't need it anymore - PUT API (Soft delete)
5. As a user I want to Search for recipes via tags
6. As a user I want to be able to see top 25 recipes in my Recently Used Category
7. As a user I want to be able to see all recipes in my Favorites Category
8. As a user I want to be able to create my own custom Category (POST API)
9. As a user I want to be able to selectively add/remove recipes to my custom Category
10. As a user I want to be able to delete my custom Category without deleting the recipes in it


A list of “stretch goals”/features
1. As a user I want to be able to use JustTheRecipe api in this app to extract a recipe from any URL.
2. Ability to invite friends, add friends and share recipes within the app
3. Create grocery lists within the app


# UML class diagram
[UML Class Diagram](RecipeoUMLClass.puml)


# DynamoDB 
List of Tables and global secondary indexes:
### User
* userId:String
* emailId:String

### Recipe
* recipeId:String (partition key)
* userId:String (sort key)
* 

### Category
* categoryId:String
* 


### 
Each item must include the partition and sort key (if applicable) and all other attributes
When listing attributes, you must include its name and DynamoDB type

# API Endpoints
A description of each API endpoint that includes:
Name
A brief description
HTTP method
Path, including any path parameters
Query string parameters (if applicable)
Request body (if applicable)
Response body
A description of any errors the endpoint might return

# UML Sequence Diagram
A UML sequence diagram of the process of at least one endpoint

# Mockups
Mockups (aka “wireframes”) of the front-end web application

# AWS Services
* CloudFormation
* S3
* CloudFront
* API Gateway
* Lambda
* Dynamo DB
