import RecipeoClient from '../api/recipeoClient';
import Header from '../components/header';
import SideNav from '../components/sidenav';
import BindingClass from "../util/bindingClass";
import DataStore from "../util/DataStore";


/**
 * Logic needed for the view recipes page of the website.
 */
class Index extends BindingClass {
    constructor() {
        super();
        this.bindClassMethods(['mount'], this);
        // Create a enw datastore with an initial "empty" state.
        this.header = new Header();

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
    const index = new Index();
    index.mount();
};

window.addEventListener('DOMContentLoaded', main);
