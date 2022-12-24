import './App.css';
import Todo from './Todo';
import AddTodo from './AddTodo'
import { useState, useEffect } from 'react';
import { Container, List, Paper } from '@mui/material';
import { call } from './ApiService';

function App() {
  const [items, setItems] = useState([]);

  useEffect(() => {
    call("/todo", "GET", null)
      .then((response) => setItems(response.data));
  }, []);

  

  const addItem = (item) => {
    // item.id = "ID-" + items.length; // key를 위한 id
    // item.done = false; // done 초기화
    // // 업데이트는 반드시 setItems로 하고 새 배열을 만들어야 함.
    // setItems([...items, item]);
    call("/todo", "POST", item)
      .then((response) => setItems(response.data));
    
      console.log("items : ", items);
  }

  const deleteItem = (item) => {
    // // 삭제할 아이템을 찾음.
    // const newItems = items.filter(e => e.id !== item.id);
    // // 삭제할 아이템을 제외한 아이템을 다시 배열에 저장
    // setItems([...newItems]);

    call("/todo", "DELETE", item)
      .then((response) => setItems(response.data));
  }

  const editItem = (item) => {
    // setItems([...items]);
    call("/todo", "PUT", item)
      .then((response) => setItems(response.data));
  }

  let todoItems = items.length > 0 && (
    <Paper style={{ margin: 16}}>
      <List>
        {items.map((item) => (
          <Todo 
            item={item} 
            key={item.id} 
            editItem = {editItem}
            deleteItem = {deleteItem} />
        ))}
      </List>
    </Paper>
  )

  return (
    <div className="App">
      <Container maxwidth="md">
        <AddTodo addItem={addItem}/>
        <div className='TodoList'>{ todoItems }</div>
      </Container>
    </div>
  );
}

export default App;
