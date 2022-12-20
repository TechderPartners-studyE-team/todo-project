import './App.css';
import Todo from './Todo';
import AddTodo from './AddTodo'
import { useState } from 'react';
import { List, Paper } from '@mui/material';

function App() {
  const [items, setItems] = useState([
    {
    id: "0",
    title: "Hello world 1",
    done: true,
    },
    {
      id: "1",
      title: "Hello world 1",
      done: true,
    },]
  );

  const addItem = (item) => {
    item.id = "ID-" + items.length; // key를 위한 id
    item.done = false; // done 초기화
    // 업데이트는 반드시 setItems로 하고 새 배열을 만들어야 함.
    setItems([...items, item]);

    console.log("items : ", items);
  }
  let todoItems = items.length > 0 && (
    <Paper style={{ margin: 16}}>
      <List>
        {items.map((item) => (
          <Todo item={item} key={item.id} />
        ))}
      </List>
    </Paper>
  )

  return (
    <div className="App">
      <AddTodo addItem={addItem}/>
      <div className='TodoList'>{ todoItems }</div>
    </div>
  );
}

export default App;
