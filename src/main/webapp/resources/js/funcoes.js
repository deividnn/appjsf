/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function removerDesabilitados() {
    $("input").removeClass("ui-state-disabled");
    $("span").removeClass("ui-state-disabled");
    $("div").removeClass("ui-state-disabled");
    $("textarea").removeClass("ui-state-disabled");
}

$(document).bind("keydown", function (e) {
    e = e || window.event;
    var key = e.which || e.charCode || e.keyCode;

    if ($(".criar").is(':disabled') === false) {
        if ((key === 117)) {
            e.preventDefault();
            $(".criar").click();
        }
    }

    if ($(".editar").is(':disabled') === false) {
        if ((key === 118)) {
            e.preventDefault();
            $(".editar").click();
        }
    }

    if ($(".ver").is(':disabled') === false) {
        if ((key === 119)) {
            e.preventDefault();
            $(".ver").click();
        }
    }

    if ($(".revisoes").is(':disabled') === false) {
        if ((key === 120)) {
            e.preventDefault();
            $(".revisoes").click();
        }
    }
});


