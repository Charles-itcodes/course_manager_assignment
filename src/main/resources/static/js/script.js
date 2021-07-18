const init = () => {
    let selector = document.getElementById('type');
    updateSearchCriteria(selector.options[selector.selectedIndex]);
}

const updateSearchCriteria = (elem) =>{
    const type = elem.value;

    let input = document.getElementById('value');

    if(type === 'name'){
        input.placeholder = 'Find by course name';
        input.type = 'text';
    }else if(type === 'courseStartBefore'){
        input.placeholder = 'Search by course start before';
        input.type = 'date';
    }else if(type === 'email'){
        input.placeholder = 'Find by email';
        input.type = 'text';
    }else if(type === 'full_name'){
        input.placeholder = 'Find by student name';
        input.type = 'text';

    }else{
        input.placeholder = 'Search by course start after';
        input.type = 'date';
    }
};

init();


