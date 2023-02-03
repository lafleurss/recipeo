import RecipeoClient from '../api/recipeoClient';
import Header from '../components/header';
import SideNav from '../components/sidenav';
import BindingClass from "../util/bindingClass";
import DataStore from "../util/DataStore";


/**
 * Logic needed for the view recipes page of the website.
 */
class ViewRecipes extends BindingClass {
    constructor() {
        super();
        this.bindClassMethods(['mount','displayRecipesOnPage'], this);
        // Create a enw datastore with an initial "empty" state.
        this.dataStore = new DataStore();
        this.dataStore.addChangeListener(this.displayRecipesOnPage);
        this.header = new Header();
        this.sidenav = new SideNav(this.dataStore);
    }

 /**
     * Once the client is loaded, get the recipes list.
     */
    async clientLoaded() {
        //Get all recipes for user API
        const recipes = await this.client.getRecipesForUser("ALL", true);
        this.dataStore.set('recipes', recipes);
        //this.dataStore.set('veryFirstEmpId', employees[0].lastNameEmployeeId);
        //this.dataStore.set('firstEmpId', employees[0].lastNameEmployeeId);
    }



/**
     * When the employees are updated in the datastore, update the list of recipes on the page.
     */
    displayRecipesOnPage() {
        const recipes = this.dataStore.get('recipes');
        if (!recipes) {
            return;
        }
        let table = document.querySelector("table");
        //Flush the table first
        var tableHeaderRowCount = 1;
        var rowCount = table.rows.length;
        for (var i = tableHeaderRowCount; i < rowCount; i++) {
            table.deleteRow(tableHeaderRowCount);
        }
        //Generate table data with the new set of recipes
        this.generateTable(table, recipes);
        //this.genCard(recipes);
        document.getElementById('recipes').innerText = "";

        if (recipes.length === 0) {
            document.getElementById('recipes').innerText = "(No recipes found...)";
        }
 }

     generateTable(table, data) {
        if (data.length != 0) {
           for (let element of data) {
             let row = table.insertRow();

             row.addEventListener('click', async evt => {
                         window.location.href = `/viewRecipeDetail.html?id=${element.recipeId}`;
                       });

             let cell = row.insertCell();
             let text = document.createTextNode(element.recipeName);
             cell.appendChild(text);

             cell = row.insertCell();
             text = document.createTextNode(element.categoryName);
             cell.appendChild(text);

             cell = row.insertCell();
             text = document.createTextNode(element.servings);
             cell.appendChild(text);

             cell = row.insertCell();
             text = document.createTextNode(element.prepTime);
             cell.appendChild(text);

            cell = row.insertCell();
            text = document.createTextNode(element.cookTime);
            cell.appendChild(text);

            cell = row.insertCell();
            text = document.createTextNode(element.totalTime);
            cell.appendChild(text);

           }
       }
     }


    /**
     * Add the header to the page and load the RecipeoClient.
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
    const viewRecipes = new ViewRecipes();
    viewRecipes.mount();
};

window.addEventListener('DOMContentLoaded', main);
