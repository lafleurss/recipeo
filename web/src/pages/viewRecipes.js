import RecipeoClient from '../api/recipeoClient';
import Header from '../components/header';
import BindingClass from "../util/bindingClass";
import DataStore from "../util/DataStore";


/**
 * Logic needed for the view recipes page of the website.
 */
class ViewRecipes extends BindingClass {
    constructor() {
        super();
        this.bindClassMethods(['mount'], this);
        // Create a enw datastore with an initial "empty" state.
        this.dataStore = new DataStore();
        this.header = new Header(this.dataStore);
    }

 /**
     * Once the client is loaded, get the recipes list.
     */
    async clientLoaded() {

        //Get all recipes for user API
        const recipes = await this.client.getRecipesForUser("shilpa.sathya+test1@gmail.com", true);
        this.dataStore.set('recipes', recipes);
        //this.dataStore.set('veryFirstEmpId', employees[0].lastNameEmployeeId);
        //this.dataStore.set('firstEmpId', employees[0].lastNameEmployeeId);
        await this.displayRecipesOnPage();
    }

/**
     * When the employees are updated in the datastore, update the list of recipes on the page.
     */
    async displayRecipesOnPage() {
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

    async genCard(recipes){
    const container = document.getElementById('accordion');

    recipes.forEach((result, idx) => {
      // Create card element
      const card = document.createElement('div');
      card.classList = 'card-body';

      // Construct card content
      const content = `
        <div class="card">
        <div class="card-header" id="heading-${idx}">
          <h5 class="mb-0">
            <button class="btn btn-link" data-toggle="collapse" data-target="#collapse-${idx}" aria-expanded="true" aria-controls="collapse-${idx}">

                    </button>
          </h5>
        </div>

        <div id="collapse-${idx}" class="collapse show" aria-labelledby="heading-${idx}" data-parent="#accordion">
          <div class="card-body">

            <h5>${result.title}</h5>
            <p>${result.description}</p>
            <p>${result.output}</p>
            ...
          </div>
        </div>
      </div>
      `;

      // Append newly created card element to the container
      container.innerHTML += content;
    })
    }

     async generateTable(table, data) {
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
                 text = document.createTextNode(element.category);
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
