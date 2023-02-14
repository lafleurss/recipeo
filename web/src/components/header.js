import RecipeoClient from '../api/recipeoClient';
import BindingClass from "../util/bindingClass";
import DataStore from "../util/DataStore";

/**
 * The header component for the website.
 */
export default class Header extends BindingClass {
    constructor() {
        super();

        const methodsToBind = [
            'addHeaderToPage', 'createSiteTitle', 'createUserInfoForHeader',
            'createLoginButton', 'createLoginButton', 'createLogoutButton'
            ,'createFaviconLink'
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
        const faviconLink = this.createFaviconLink();

        const header = document.getElementById('header');
        header.appendChild(siteTitle);
        header.appendChild(userInfo);

        const head = document.getElementById('head');
        head.appendChild(faviconLink);

        const token = await this.client.getTokenOrThrow("Please log in!");

        if (window.location.pathname == "/index.html" || window.location.pathname == "/" ){
            if (token){
             window.location.href = `/viewRecipes.html?filterType=ALL`;
            }
        }


    }

    createFaviconLink() {
        const faviconLink = document.createElement('link');

        faviconLink.setAttribute("href", "/favicon.png");
        faviconLink.setAttribute("rel", "icon");
        faviconLink.setAttribute("type", "image/x-icon");

        return faviconLink;
    }

    createSiteTitle() {
        const homeButton = document.createElement('a');
        homeButton.classList.add('header_home');
        homeButton.href = '/index.html';

        const imageLogo = document.createElement('img');
        imageLogo.classList.add('img');
        imageLogo.src = "/logo.png";
        homeButton.appendChild(imageLogo);

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

}
