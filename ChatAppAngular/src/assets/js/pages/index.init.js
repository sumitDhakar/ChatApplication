$(document).ready(function(){$(".popup-img").magnificPopup({type:"image",closeOnContentClick:!0,mainClass:"mfp-img-mobile",image:{verticalFit:!0}}),$("#user-status-carousel").owlCarousel({items:4,loop:!1,margin:16,nav:!1,dots:!1}),$("#chatinputmorelink-carousel").owlCarousel({items:2,loop:!1,margin:16,nav:!1,dots:!1,responsive:{0:{items:2},600:{items:5,nav:!1},992:{items:8}}}),$("#user-profile-hide").click(function(){$(".user-profile-sidebar").hide()}),$(".user-profile-show").click(function(){$(".user-profile-sidebar").show()}),$(".chat-user-list li a").click(function(){$(".user-chat").addClass("user-chat-show")}),$(".user-chat-remove").click(function(){$(".user-chat").removeClass("user-chat-show")})});
$(document).ready(function() {
    // Initialize Magnific Popup for images
    $(".popup-img").magnificPopup({
        type: "image",
        closeOnContentClick: true,
        mainClass: "mfp-img-mobile",
        image: {
            verticalFit: true
        }
    });

    // Initialize Owl Carousel for user-status-carousel
    $("#user-status-carousel").owlCarousel({
        items: 4,
        loop: false,
        margin: 16,
        nav: false,
        dots: false
    });

    // Initialize Owl Carousel for chatinputmorelink-carousel
    $("#chatinputmorelink-carousel").owlCarousel({
        items: 2,
        loop: false,
        margin: 16,
        nav: false,
        dots: false,
        responsive: {
            0: { items: 2 },
            600: { items: 5, nav: false },
            992: { items: 8 }
        }
    });

    // Handle user-profile-hide click event
    $("#user-profile-hide").click(function() {
       
        $(".user-profile-sidebar").hide();
    });


     // Handle user-profile-hide click event
     $("#user-profile-hide1").click(function() {
       
        $(".user-profile-sidebar").hide();
    });

    // Handle user-profile-show click event
    $(".user-profile-show").click(function() {
        $(".user-profile-sidebar").show();
    });

    // Handle chat-user-list item click event
    $(".chat-user-list li a").click(function() {
        $(".user-chat").addClass("user-chat-show");
    });

    // Handle user-chat-remove click event
    $(".user-chat-remove").click(function() {
        $(".user-chat").removeClass("user-chat-show");
    });
});
