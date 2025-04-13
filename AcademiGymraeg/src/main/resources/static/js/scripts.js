/*!
* Start Bootstrap - Creative v7.0.7 (https://startbootstrap.com/theme/creative)
* Copyright 2013-2023 Start Bootstrap
* Licensed under MIT (https://github.com/StartBootstrap/startbootstrap-creative/blob/master/LICENSE)
*/
//
// Scripts
// 

// window.addEventListener('DOMContentLoaded', event => {
//
//     // Navbar shrink function
//     var navbarShrink = function () {
//         const navbarCollapsible = document.body.querySelector('#mainNav');
//         if (!navbarCollapsible) {
//             return;
//         }
//         if (window.scrollY === 0) {
//             navbarCollapsible.classList.remove('navbar-shrink')
//         } else {
//             navbarCollapsible.classList.add('navbar-shrink')
//         }
//
//     };
//
//     // Shrink the navbar
//     navbarShrink();
//
//     // Shrink the navbar when page is scrolled
//     document.addEventListener('scroll', navbarShrink);
//
//     // Activate Bootstrap scrollspy on the main nav element
//     const mainNav = document.body.querySelector('#mainNav');
//     if (mainNav) {
//         new bootstrap.ScrollSpy(document.body, {
//             target: '#mainNav',
//             rootMargin: '0px 0px -40%',
//         });
//     };
//
//     // Collapse responsive navbar when toggler is visible
//     const navbarToggler = document.body.querySelector('.navbar-toggler');
//     const responsiveNavItems = [].slice.call(
//         document.querySelectorAll('#navbarResponsive .nav-link')
//     );
//     responsiveNavItems.map(function (responsiveNavItem) {
//         responsiveNavItem.addEventListener('click', () => {
//             if (window.getComputedStyle(navbarToggler).display !== 'none') {
//                 navbarToggler.click();
//             }
//         });
//     });
//
//     // Activate SimpleLightbox plugin for portfolio items
//     new SimpleLightbox({
//         elements: '#portfolio a.portfolio-box'
//     });
//
// });
function checkAuth() {
    const userData = JSON.parse(localStorage.getItem('userData'));

    // If no user data or expired (e.g., after 24 hours)
    if (userData == null ) {

        if(!window.location.href.includes("login") &&  !window.location.href.includes("home") ) {
            window.location.href = '/login' ;
        }
        return false;
    }
    else
    {
        if((Date.now() - userData.lastLogin) > 86400000)
        {
            localStorage.removeItem('userData');
            if(!window.location.href.includes("login") || !window.location.href.includes("home") ) {
                window.location.href = '/login' ;
            }
        }
        else {
            if(window.location.href.includes("login"))
            {
                const roleName = userData.roleName;
                window.history.replaceState(null, '', getDashboardUrl(roleName));
                window.location.href = getDashboardUrl(roleName);
            }
        }
    }

    return true;
}
document.addEventListener('DOMContentLoaded', function() {
    if (!checkAuth()) return;

    // Optional: Display user info in UI
    const userData = JSON.parse(localStorage.getItem('userData'));
    console.log('Logged in as:', userData.fullName, '-', userData.roleName);
});
window.addEventListener('popstate', function(event) {
    const userData = localStorage.getItem('userData');
    if (userData) {
        const roleName = JSON.parse(userData).roleName;
        window.history.replaceState(null, '', getDashboardUrl(roleName));
        window.location.href = getDashboardUrl(roleName);
    }
});
window.getDashboardUrl = function(roleName) {
    switch(roleName) {
        case 'ADMINISTRATOR': return '/admin-dashboard';
        case 'INSTRUCTOR': return '/instructor-dashboard';
        case 'STUDENT': return '/student-dashboard';
        default: return '/dashboard';
    }
};
function logout() {
    // Clear all authentication data
    localStorage.removeItem('userData');

    // Replace history state and redirect
    if(!window.location.href.includes("login") || !window.location.href.includes("home") )
    {
        window.history.replaceState(null, '', '/login');
        window.location.href = '/login';
    }

}
const MAX_SESSION_AGE = 24 * 60 * 60 * 1000; // 24 hours
const userData = localStorage.getItem('userData');
if(userData )
{
    if ( Date.now() - userData.lastLogin > MAX_SESSION_AGE) {
        logout();
    }
}
