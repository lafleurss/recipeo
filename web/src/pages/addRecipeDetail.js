import RecipeoClient from '../api/recipeoClient';
import Header from '../components/header';
import SideNav from '../components/sidenav';
import BindingClass from "../util/bindingClass";
import DataStore from "../util/DataStore";


/**
 * Logic needed for the save recipe page of the website.
 */
class AddRecipeDetail extends BindingClass {
    constructor() {
        super();
        this.bindClassMethods(['mount', 'saveRecipe', 'loadCategoryDropDown'], this);
        // Create a enw datastore with an initial "empty" state.
        this.dataStore = new DataStore();
        this.dataStore.addChangeListener(this.saveRecipe);

        document.getElementById('favorite').addEventListener('click', this.toggleHeart);
        document.getElementById('save_recipe').addEventListener('click', this.saveRecipe);
        this.header = new Header();
        this.sidenav = new SideNav(this.dataStore);
    }

 /**
     * Once the client is loaded, display the recipe details.
     */
    async clientLoaded() {
        this.loadCategoryDropDown();
    }

    toggleHeart() {
        if(document.getElementById('favorite').className == "fa fa-heart-o"){
            document.getElementById('favorite').className = "fa fa-heart";
        } else {
            document.getElementById('favorite').className = "fa fa-heart-o";
        }
    }

    async loadCategoryDropDown(){
        const categoriesList = await this.client.getCategoriesForUser();
        var categoryDropDown = document.getElementById('category');
        if (categoriesList) {
           for (let key of categoriesList) {
              let option = document.createElement("option");
              option.setAttribute('value', key.categoryName);
              option.setAttribute('innerHTML', key.categoryName);
              let optionText = document.createTextNode(key.categoryName);
              option.appendChild(optionText);
              categoryDropDown.appendChild(option);
            }
        }
    }

/**
     * When the recipe is  saved in the datastore, update recipe details DOM on the page.
     */
    async saveRecipe() {
        const nameRegex = new RegExp('[^a-zA-Z\\s-\'.]');
                const recipeName = document.getElementById('recipename').value;
                const servings =  document.getElementById('servings').value;
                const preptime =  document.getElementById('preptime').value;
                const cooktime =  document.getElementById('cooktime').value;
                const totaltime =  document.getElementById('totaltime').value;
                const categoryElement = document.getElementById('category');
                const categoryName = categoryElement.options[categoryElement.selectedIndex].innerHTML;
                const favoriteClassName = document.getElementById('favorite').className;

                if (favoriteClassName == "fa fa-heart-o") {
                    const isFavorite = "false";
                } else if (favoriteClassName == "fa fa-heart"){
                    const isFavorite = "true";
                }

                const instructions = document.getElementById('instructions').innerText;
                const ingredients = document.getElementById('ingredients').innerText;

                if (!recipeName || !servings || !preptime || !cooktime || !totaltime || !ingredients || !instructions) {
                    alert("Please fill in all required fields");
                    return;
                }

                if (nameRegex.test(recipeName)) {
                    alert("The first name you entered has invalid characters");
                    return;
                }

                let payload = {recipeName: recipeName, servings: servings, prepTime: prepTime, cookTime: cookTime,
                totalTime : totalTime, ingredients : ingredients, instructions : instructions,
                tags : tags, isFavorite : isFavorite, categoryName : categoryName};

                document.getElementById('save_recipe').disabled = true;
                document.getElementById('save_recipe').innerHTML = 'Saving Recipe...';
                document.getElementById('save_recipe').style.background='grey';
                const recipe = await this.client.createRecipe(payload);
                this.dataStore.set('recipe', recipe);



//        if (recipe.ingredients){
//            var list = document.getElementById('ingredients')
//            var counter = 0;
//            for (var i in recipe.ingredients) {
//              var elem = document.createElement("li");
//              elem.innerText =  recipe.ingredients[i];
//              list.appendChild(elem);
//            }
//        }

 }



    /**
     * Add the header to the page and load the MusicPlaylistClient.
     */
    async mount() {
        this.header.addHeaderToPage();
        this.sidenav.addSideNavToPage();

        this.client = new RecipeoClient();
        await this.clientLoaded();
    }
}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const addRecipeDetail = new AddRecipeDetail();
    addRecipeDetail.mount();
};

window.addEventListener('DOMContentLoaded', main);
