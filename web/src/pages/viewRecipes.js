import RecipeoClient from '../api/recipeoClient';
import Header from '../components/header';
import SideNav from '../components/sidenav';
import BindingClass from "../util/bindingClass";
import DataStore from "../util/DataStore";

const SEARCH_CRITERIA_KEY = 'search-criteria';
const SEARCH_RESULTS_KEY = 'search-results';
const EMPTY_DATASTORE_STATE = {
    [SEARCH_CRITERIA_KEY]: '',
    [SEARCH_RESULTS_KEY]: [],
};

/**
 * Logic needed for the view recipes page of the website.
 */
class ViewRecipes extends BindingClass {
    constructor() {
        super();
        this.bindClassMethods(['mount','displayRecipesOnPage', 'search'], this);
        this.dataStore = new DataStore();
        this.dataStore.addChangeListener(this.displayRecipesOnPage);
        this.header = new Header();
        this.sidenav = new SideNav(this.dataStore);

        var link = window.location.href.toString();
        if (link.includes("/viewRecipes.html")) {
            document.getElementById("search_button").addEventListener('click', this.search);
        }

    }

 /**
     * Once the client is loaded, get the recipes list.
     */
    async clientLoaded() {
        await this.sidenav.sideNavRedirects();
    }

/**
     * Uses the client to perform the search,
     * then updates the datastore with the criteria and results.
     * @param evt The "event" object representing the user-initiated event that triggered this method.
     */
    async search(evt) {
        document.getElementById('search_button').value = "Searching...";
        document.getElementById('search_button').disabled = true;
        document.getElementById('search_button').style.background='grey';

        const searchCriteria = document.getElementById('search_criteria').value;
        const previousSearchCriteria = this.dataStore.get(SEARCH_CRITERIA_KEY);

        // If the user didn't change the search criteria, do nothing
        if (previousSearchCriteria === searchCriteria) {
            return;
        }

        if (searchCriteria) {
            const results = await this.client.search(searchCriteria);

            if (!results || results.length === 0) {
                this.dataStore.set('recipes', undefined);
                document.getElementById('recipes').innerText = "No recipes found...";
            } else {
                this.dataStore.set('recipes', results);
            }
        }
        document.getElementById('search_button').value = "Search";
        document.getElementById('search_button').disabled = false;
        document.getElementById('search_button').style.background='#f0bab9';

    }


/**
     * When the recipes are updated in the datastore, update the list of recipes on the page.
     */
    displayRecipesOnPage() {
        document.getElementById('spinner-recipe').style.display = "inline";
        const recipes = this.dataStore.get('recipes');

        //Flush the table first
        let table = document.querySelector("table");
        var tableHeaderRowCount = 1;
        var rowCount = table.rows.length;
        for (var i = tableHeaderRowCount; i < rowCount; i++) {
            table.deleteRow(tableHeaderRowCount);
        }

        if (!recipes || recipes.length === 0) {
            document.getElementById('recipes').innerText = "No recipes found...";
        } else {
            //Generate table data with the new set of recipes
            this.generateTable(table, recipes);
            document.getElementById('recipes').innerText = "";
        }
        document.getElementById('spinner-recipe').style.display = "none";
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
