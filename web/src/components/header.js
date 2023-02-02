import RecipeoClient from '../api/recipeoClient';
import BindingClass from "../util/bindingClass";

/**
 * The header component for the website.
 */
export default class Header extends BindingClass {
    constructor() {
        super();

        const methodsToBind = [
            'addHeaderToPage', 'createSiteTitle', 'createUserInfoForHeader',
            'createLoginButton', 'createLoginButton', 'createLogoutButton'
            ,'loadCategories', 'addCategory'
        ];
        this.bindClassMethods(methodsToBind, this);

        this.client = new RecipeoClient();
    }

    /**
     * Add the header to the page.
     */
    async addHeaderToPage() {
        const currentUser = await this.client.getIdentity();

        const siteTitle = this.createSiteTitle();
        const userInfo = this.createUserInfoForHeader(currentUser);
        //Loads categories in the side nav bar
        this.loadCategories();

        const header = document.getElementById('header');
        header.appendChild(siteTitle);
        header.appendChild(userInfo);
    }

    createSiteTitle() {
        const homeButton = document.createElement('img');
        homeButton.classList.add('header_home');
        homeButton.href = 'index.html';
        homeButton.innerText = 'RECIPEO';
        homeButton.src = "/logo.png"

        const siteTitle = document.createElement('div');
        siteTitle.classList.add('site-title');
        siteTitle.appendChild(homeButton);

        return siteTitle;
    }

    createUserInfoForHeader(currentUser) {
        const userInfo = document.createElement('div');
        userInfo.classList.add('user');

        const childContent = currentUser
            ? this.createLogoutButton(currentUser)
            : this.createLoginButton();

        userInfo.appendChild(childContent);

        return userInfo;
    }

    createLoginButton() {
        return this.createButton('Login', this.client.login);
    }

    createLogoutButton(currentUser) {
        return this.createButton(`Logout: ${currentUser.name}`, this.client.logout);
    }

    createButton(text, clickHandler) {
        const button = document.createElement('a');
        button.classList.add('button');
        button.href = '#';
        button.innerText = text;

        button.addEventListener('click', async () => {
            await clickHandler();
        });

        return button;
    }

    async loadCategories(){
        const categoriesList = await this.client.getCategoriesForUser();
        var divElement = document.getElementById('sidenav_custom_categories');

        for (let element of categoriesList) {
            // Create anchor element.
            var a = document.createElement('a');
            // Create the text node for anchor element.
            var link = document.createTextNode(element.categoryName);
            // Append the text node to anchor element.
            a.appendChild(link);
            // Set the title.
            a.title = link;
            // Set the href property.
            //a.href = "https://www.geeksforgeeks.org";
            // Append the anchor element to the body.
            divElement.append(a);
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
}
