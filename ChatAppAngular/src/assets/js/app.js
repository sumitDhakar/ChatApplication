!function (e) { "use strict"; var o, t; e(".dropdown-menu a.dropdown-toggle").on("click", function (t) { return e(this).next().hasClass("show") || e(this).parents(".dropdown-menu").first().find(".show").removeClass("show"), e(this).next(".dropdown-menu").toggleClass("show"), !1 }), [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]')).map(function (t) { return new bootstrap.Tooltip(t) }), [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]')).map(function (t) { return new bootstrap.Popover(t) }), o = document.getElementsByTagName("body")[0], (t = document.querySelectorAll(".light-dark-mode")) && t.length && t.forEach(function (t) { t.addEventListener("click", function (t) { o.hasAttribute("data-bs-theme") && "dark" == o.getAttribute("data-bs-theme") ? document.body.setAttribute("data-bs-theme", "light") : document.body.setAttribute("data-bs-theme", "dark") }) }), Waves.init() }(jQuery);

// !function(e) {
//     "use strict";
//     var o, t;
//     e(".dropdown-menu a.dropdown-toggle").on("click", function(t) {
//         return e(this).next().hasClass("show") || e(this).parents(".dropdown-menu").first().find(".show").removeClass("show"),
//         e(this).next(".dropdown-menu").toggleClass("show"),
//         !1
//     }),
//     alert("asdf");
//     [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]')).map(function(t) {
//         return new bootstrap.Tooltip(t)
//     }),
//     [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]')).map(function(t) {
//         return new bootstrap.Popover(t)
//     }),
//     o = document.getElementsByTagName("body")[0],
//     (t = document.querySelectorAll(".light-dark-mode")) && t.length && t.forEach(function(t) {
//         t.addEventListener("click", function(t) {
//             o.hasAttribute("data-bs-theme") && "dark" == o.getAttribute("data-bs-theme") ? document.body.setAttribute("data-bs-theme", "light") : document.body.setAttribute("data-bs-theme", "dark")
//         })
//     }),
//     Waves.init()
// }(jQuery);
// Wrap your script in a function for better organization
function initializeScript() {
    "use strict";

    // Handle dropdown toggles
    $(".dropdown-menu a.dropdown-toggle").on("click", function(e) {
        e.stopPropagation();
        $(this).next().toggleClass("show");
    });

    // Initialize tooltips
    $('[data-bs-toggle="tooltip"]').tooltip();

    // Initialize popovers
    $('[data-bs-toggle="popover"]').popover();

    // Handle light/dark mode toggle
    var body = document.getElementsByTagName("body")[0];
    var lightDarkModeElements = document.querySelectorAll(".light-dark-mode");

    if (lightDarkModeElements && lightDarkModeElements.length) {
        lightDarkModeElements.forEach(function(element) {
            element.addEventListener("click", function() {
                if (body.hasAttribute("data-bs-theme") && body.getAttribute("data-bs-theme") === "dark") {
                    body.setAttribute("data-bs-theme", "light");
                } else {
                    body.setAttribute("data-bs-theme", "dark");
                }
            });
        });
    }

    // Initialize Waves effect
    Waves.init();
}

// Run the script when the DOM is ready
document.addEventListener("DOMContentLoaded", function() {
    initializeScript();
});




