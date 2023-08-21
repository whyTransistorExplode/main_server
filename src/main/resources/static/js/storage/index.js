const root_div = document.getElementById("root-div");
const root_path_display = document.getElementById("root-path-text");
const tree_div = document.getElementById('tree-div');
const data = {index: -1, selected_path: []};
const treeData = [{file: false, listFiles: [], name: "", path: "/"}];

const folder_img = "http://localhost:2354/pictures/folder.png";

const file_img = "http://localhost:2354/pictures/file.png";

function start() {

    configure();
    root_call("/");

}

//** this asynchronously make calls to server and retrieve path directory that are allowed for this specific user
function root_call(path) {

    authorizedAjaxCall("path=" + path,
        "GET"
        , '/api/v1/share/path'
        , function (result) {

            if (!result.success) {
                return;
            }

            const arrayL = result.content;
            data.selected_path.push(path);
            display_path();
            draw_div(arrayL);

            console.log(insertTreeData(treeData, path, arrayL))
            console.log(treeData);
            // draw_tree_div(arrayL, path);
        }
    )
}

//** this asynchronously make calls to server and retrieve path directory that are allowed for this specific user
//** but this one is for backwards for back button presses
function root_call_back(path) {
    authorizedAjaxCall('path=' + path, "GET"
        , '/api/v1/share/path'
        , function (result) {
            if (!result.success) {
                // list_root_call(); return;
                return;
            }

            data.selected_path.pop();
            display_path();

            const arrayL = result.content;
            draw_div(arrayL)

        }
    )
}

//** this will draw list of paths to the root element
function draw_div(pathList) {
    root_div.innerHTML = "";

    data.list = pathList;
    let v, p;
    let s;

    v = document.createElement("div");//'<div class="root-item" onclick="backward()"> .. </div>';
    v.classList.add("root-item");
    v.addEventListener("click", backward);
    p = document.createElement("p");
    p.classList.add("root-item-p");
    p.innerText = "..";
    v.appendChild(p);
    root_div.appendChild(v);

    for (let i = 0; i < pathList.length; i++) {
        s = pathList[i];

        // console.log(folder)
        v = document.createElement("div");
        v.classList.add("root-item");
        v.addEventListener("click", () => forward(i));
        // v.innerText = "..";
        p = document.createElement("p");
        p.classList.add("root-item-p");
        // console.log(s.file);
        p.append((s.name ? s.name : s.path));
        v.appendChild((s.file ? imageProvider(file_img, "root-item-img") : imageProvider(folder_img, "root-item-img")))
        v.appendChild(p);
        // v += '<div class="root-item" onclick="forward(' + i + ')" ><p class="root-item-p">' + (s.name ? s.name : s.path) + '</p></div>';
        root_div.appendChild(v);
    }
    // root_div.append(folder)
}


const tree_ul = document.createElement("ul");
tree_ul.classList.add("tree-view-ul");
tree_ul.id = "tree-view-ul";

function draw_tree_div(tree_paths) {
    tree_div.innerHTML = "";
    tree_div.appendChild(tree_ul);


}

function recursive_tree_div(tree_list_data, node) {
    if (tree_list_data == null) return node;


}


function forward(id) {
    const path = map_path(data.list[id].path);
    // ++data.index;
    // data.selected_path.push(path);
    root_call(path);
}

function backward() {
    // if (data.selected_path.length <= 1) {
    //     data.selected_path = [];
    //     list_root_call();
    // return;
    // }

    let cut = data.selected_path[data.selected_path.length - 2];
    // let cut = data.selected_path[data.index];
    root_call_back(cut);
}


function map_path(path) {
    let collector = "";
    for (let i = 0; i < path.length; i++) {
        if (path[i] === '\\') collector += '/';
        else collector += path[i];
    }
    return collector;
}

function display_path() {

    //** if the last selected path is equal to '/' then we just display a little bit more explanation '/allowed_path'
    root_path_display.innerHTML =
        (data.selected_path[data.selected_path.length - 1] === '/' ? '/allowed_paths' : data.selected_path[data.selected_path.length - 1]);
}

function configure() {
    const d = getConfigure('tree_width');
    if (d != null)
        tree_div.style.width = d;

}

function addConfigure(key, value) {
    window.localStorage.setItem('conf:' + key, value);
}

function getConfigure(key) {
    return window.localStorage.getItem('conf:' + key)
}


function imageProvider(href, classes) {
    let b = document.createElement("img");
    b.src = href;
    b.classList.add(classes);
    return b;
}

function LIProvider(obj) {
    let li = document.createElement("li");
    let span = document.createElement("span");
    span.classList.add("caret")
    span.innerText = obj.name;
    li.appendChild(span);

    // let ul = document.createElement("ul");
    // ul.classList.add("nested");
    // li.appendChild(ul);
    return li;
}

function nestedUlProvider(obj) {
    let ul = document.createElement("ul");
    ul.classList.add("nested");
}

//** arrayL: successResult data, tree_Data: main stored listDAta, path: ??? idk how to describe
function insertTreeData(tree_data, path, arrayL) {
    console.log(path);

    for (let i = 0; i < tree_data.length; i++) {
        let b = tree_data[i];

        if (b.path !== path) continue;

        b.listFiles = arrayL;
        return true;
    }

    for (let i = 0; i < tree_data.length; i++) {
        let b = tree_data[i];
        if (insertTreeData(b.listFiles, path, arrayL)) return true;
    }

}

start();