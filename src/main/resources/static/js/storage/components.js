function imageProvider(href, ...classes) {
    let b = document.createElement("img");
    b.src = href;
    b.classList.add(classes);
    return b;
}

function LIProvider(obj) {
    const li = document.createElement("li");
    const div_container = document.createElement("div");
    const span = document.createElement("span");
    const tree_folder_img = imageProvider("http://localhost:2354/pictures/tree_folder_closed.png", "tree-item-img");//document.createElement("img");
    // tree_folder_img.src = "http://localhost:2354/pictures/tree_folder_closed.png";
    // tree_folder_img.classList.add("tree-item-img");

    div_container.classList.add("tree-caret-container");
    div_container.appendChild(tree_folder_img);
    div_container.appendChild(span);
    span.classList.add("caret", "tree-item-span");

    tree_folder_img.addEventListener("click", function () {



        let flag = this.parentElement.parentElement.querySelector(".nested").classList.toggle("active");
        if (flag) {
            this.src = "http://localhost:2354/pictures/tree_folder_open.png";
            // const s = isContentExistByPath(obj.path);
            // console.log("bef");
            // console.log(getContentByPath(obj.path))
            if (!isContentExistByPath(obj.path)){
                root_call(obj.path)
            }
        }else
            this.src = "http://localhost:2354/pictures/tree_folder_closed.png";

        this.classList.toggle("caret-down");

    });

    span.innerText = (obj.name ? obj.name : obj.path);
    li.appendChild(div_container);

    // let ul = document.createElement("ul");
    // ul.classList.add("nested");
    // li.appendChild(ul);
    return li;
}

function nestedUlProvider() {
    const ul = document.createElement("ul");
    ul.classList.add("nested", "tree-view-ul");

    return ul;
}