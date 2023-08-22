const root_div = document.getElementById("root-div");
const root_path_display = document.getElementById("root-path-text");
const tree_div = document.getElementById('tree-div');
const data = {index: -1, selected_path: []};
const treeData = [{file: false, listFiles: [], name: "", path: "/", state: false, node: null}];

const folder_img = "http://localhost:2354/pictures/folder.png";

const file_img = "http://localhost:2354/pictures/file.png";

function start() {
    configure();
    initialize_tree_hierarchy();
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

            console.log(insertTreeData(treeData, path, arrayL));
            console.log(treeData);

            draw_tree_div(arrayL, path);
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
function initialize_tree_hierarchy(){
    tree_ul.classList.add("tree-view-ul");
    tree_ul.id = "tree-view-ul";

    tree_div.innerHTML = "";
    tree_div.appendChild(tree_ul);
}
function draw_tree_div() {

    recursive_tree_div(treeData, tree_ul)
}



function recursive_tree_div(tree_list_data, node) {
    if (tree_list_data == null) return;

    for (let i = 0; i < tree_list_data.length; i++) {
        const b = tree_list_data[i];

        if (b.state) {
            recursive_tree_div(b.listFiles, b.node);
            continue;
        }

        if (b.node != null) {
            const oldLI = b.node.parentElement;
            oldLI.querySelector(".tree-item-span").innerHTML = (b.name ? b.name : b.path);
            b.node.innerHTML = "";
            recursive_tree_div(b.listFiles, b.node);
            // oldLI.replaceWith(li);
        }
        else {
            const li = LIProvider(b);
            const ul = nestedUlProvider();

            li.appendChild(ul);
            node.appendChild(li);
            b.node = ul;
            recursive_tree_div(b.listFiles, b.node);
        }

        b.state = true;
    }
}


function forward(id) {
    const path = (data.list[id].path);
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


// function map_path(path) {
//     let collector = "";
//     for (let i = 0; i < path.length; i++) {
//         if (path[i] === '\\') collector += '/';
//         else collector += path[i];
//     }
//     return collector;
// }

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

//** arrayL: successResult data, tree_Data: main stored listDAta, path: ??? idk how to describe


function isContentExistByPath(path) {
    const res = getObjectByPathTreeData(treeData, path).listFiles;
    return (res != null && res.length > 0);
}
function getContentByPath(path) {
    return getObjectByPathTreeData(treeData, path).listFiles;
}

function getObjectByPathTreeData(tree_data, path){
    for (let i = 0; i < tree_data.length; i++) {
        const b = tree_data[i];

        if (b.path !== path) continue;
        return b;
    }

    for (let i = 0; i < tree_data.length; i++) {
        const b = tree_data[i];
        const res = getObjectByPathTreeData(b.listFiles, path);

        if (res) return res;
    }
}
function insertTreeData(tree_data, path, arrayL) {

    for (let i = 0; i < tree_data.length; i++) {
        const b = tree_data[i];

        //found node
        if (b.path !== path) continue;

        let index = -1;
        for (let j = 0; j < arrayL.length; j++) {
            if (arrayL[j].file) continue;
            b.listFiles[++index] = arrayL[j];
            b.state = false;
        }
        // b.listFiles = arrayL;
        return true;
    }

    for (let i = 0; i < tree_data.length; i++) {
        let b = tree_data[i];
        if (insertTreeData(b.listFiles, path, arrayL)) return true;
    }

}



start();