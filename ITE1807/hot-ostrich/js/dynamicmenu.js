/* document.addEventListener(
    "DOMContentLoaded", () => {
        const menu = new Mmenu( "#my-menu" );
        const api = menu.API;
    }
);

const panel = document.querySelector( "#my-list" );
const listview = panel.querySelector( ".mm-listview" );

const listitem = document.createElement( "li" );
listitem.innerHTML = `<a href="/work">Our work</a>`;

listview.append( listitem );
api.initListview( listview );

const panel = document.querySelector( "#my-list" );
const listitem = document.querySelector( "#my-item" );

const listview = document.createElement( "ul" );
listview.innerHTML = `
        <li><a href="/about/history">History</a></li>
        <li><a href="/about/team">The team</a></li>
        <li><a href="/about/address">Our address</a></li>`;

listitem.append( listview );
api.initPanel( panel );

 */


Mmenu.configs.offCanvas.page.selector = "#my-page";

document.addEventListener(
    "DOMContentLoaded", () => {
        const menu = new Mmenu( "#my-menu" );
        const api = menu.API;

        document.querySelector( "#add_li" )
            .addEventListener(
                "click", ( evnt ) => {
                    evnt.preventDefault();

                    //    Find the panel and listview.
                    const panel = document.querySelector( "#my-list" );
                    const listview = panel.querySelector( ".mm-listview" );

                    //    Create the new listitem.
                    const listitem = document.createElement( "li" );
                    listitem.innerHTML = `<a href="/work">Our work</a>`;

                    //    Add the listitem to the listview.
                    listview.append( listitem );

                    //    Update the listview.
                    api.initListview( listview );
                }
            );

        document.querySelector( "#add_ul" )
            .addEventListener(
                "click", ( evnt ) => {
                    evnt.preventDefault();

                    //    Find the panel and listitem.
                    const panel = document.querySelector( "#my-list" );
                    const listitem = document.querySelector( "#my-item" );

                    //    Create the new listview.
                    const listview = document.createElement( "ul" );
                    listview.innerHTML = `
                                    <li><a href="/about/history">History</a></li>
                                    <li><a href="/about/team">The team</a></li>
                                    <li><a href="/about/address">Our address</a></li>`;

                    //    Add the listview to the listitem.
                    listitem.append( listview );

                    //    Update the listview.
                    api.initPanel( panel );
                }
            );
    }
);