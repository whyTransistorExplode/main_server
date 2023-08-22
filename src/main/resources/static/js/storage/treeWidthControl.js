const in_between_div = document.getElementById("in-between");


function initialize() {
    set_in_between_state();
    //treeview button toggling

}

function set_in_between_state() {
    let state_in_between = {offset_width : 0, isPressed: false};


    $("#in-between").on("mousedown", function (e) {
        const offsetLeft = in_between_div.offsetLeft;
        state_in_between.offset_width = e.clientX - offsetLeft;
        state_in_between.isPressed = true;
        $("html").css("cursor", "e-resize");
    });

    document.addEventListener("mousemove", function (e) {

        // if (e.which !== 1) return;

        // const localY = e.clientY - e.target.offsetTop;
        // console.log(tree_div.style.width);

        if (state_in_between.isPressed)
        {
            e.preventDefault();
            //todo width = element.x- mouse_target.x;
            const localX = e.clientX - tree_div.offsetLeft - state_in_between.offset_width;
            tree_div.style.width = localX+"px";
            // console.log(tree_div)
            // console.log("tree-div object:");
            // console.log(tree_div);
            // console.log(tree_div.x + "/" + e.clientX +" | " + Math.abs(tree_div.x - e.clientX).toString())
            // console.log("mouse.object:");
            // console.log(e);
            // tree_div.style.setProperty('width', Math.abs(tree_div.x - e.clientX).toString())
        }

    });
    document.addEventListener("mouseup", function () {
        state_in_between.isPressed = false;
        addConfigure('tree_width', tree_div.style.width)
        $("html").css("cursor", "default");
    });
}


initialize();