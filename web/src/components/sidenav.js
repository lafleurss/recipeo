import RecipeoClient from '../api/recipeoClient';
import BindingClass from "../util/bindingClass";
import DataStore from "../util/DataStore";

/**
 * The sidenav component for the website.
 */
export default class SideNav extends BindingClass {
    constructor(dataStore) {
        super();

        const methodsToBind = [
            'loadCategories', 'addCategory', 'viewFavorites', 'viewRecent'
            ,'viewUncategorized', 'viewCategory'
        ];
        this.bindClassMethods(methodsToBind, this);
        this.dataStore = dataStore;
        this.client = new RecipeoClient();

        //Event listeners for side
        document.getElementById('add_category').addEventListener('click', this.addCategory);
        document.getElementById('favorite_recipes').addEventListener('click', this.viewFavorites);
        document.getElementById('recent_recipes').addEventListener('click', this.viewRecent);
        document.getElementById('uncategorized_recipes').addEventListener('click', this.viewUncategorized);
    }

    async addSideNavToPage(){
        await this.loadCategories();
    }

    async loadCategories(){
        const categoriesList = await this.client.getCategoriesForUser();
        var divElement = document.getElementById('sidenav_custom_categories');
        if (categoriesList) {
            for (let element of categoriesList) {
                // Create anchor element.
                var a = document.createElement('a');
                // Create the text node for anchor element.
                var link = document.createTextNode(element.categoryName);
                // Append the text node to anchor element.
                a.appendChild(link);
                // Set the title.
                a.title = element.categoryName ;

                // Append the anchor element to the body.
                divElement.append(a);

                // Set the event listener property.
                a.addEventListener('click', async () => await this.viewCategory(element.categoryName));
            }
        }
    }

    async addCategory(){
        const nameRegex = new RegExp('[^a-zA-Z\\s-\'.]');
        const categoryName = document.getElementById('new_category').value;
        const categoryDescription = categoryName;

        if (!categoryName) {
            alert("Please fill a valid Category Name");
            return;
        }
        let payload = {categoryName: categoryName, categoryDescription: categoryDescription}
        payload.categoryName = categoryName;
        payload.categoryDescription = categoryDescription;

        const categories = await this.client.addCategory(payload);
        document.getElementById('sidenav_custom_categories').innerHTML = '';
        this.loadCategories();
        document.getElementById('new_category').value = "";
    }


    async viewFavorites(){
        const recipes = await this.client.getRecipesForUser("FAVORITES");
        this.dataStore.set('recipes', recipes);
    }

    async viewRecent(){
        const recipes = await this.client.getRecipesForUser("RECENTLY_USED");
        this.dataStore.set('recipes', recipes);
    }

    async viewUncategorized(){
        const recipes = await this.client.getRecipesForUser("UNCATEGORIZED");
        this.dataStore.set('recipes', recipes);
    }

    async viewCategory(categoryName){
        //alert("Clicked " + categoryName);
        const recipes = await this.client.getRecipesForUserInCategory(categoryName);
        this.dataStore.set('recipes', recipes);
    }
}
