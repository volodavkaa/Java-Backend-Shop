import React, { useEffect, useState } from 'react';
import { Container, Table, Button, Form, Row, Col } from 'react-bootstrap';
import api from '../api/api';

interface User {
  id: number;
  email: string;
  role: string;
}

interface Category {
  id: number;
  name: string;
}

const AdminPanel: React.FC = () => {
  // Стан для керування користувачами
  const [users, setUsers] = useState<User[]>([]);
  
  // Стан для керування категоріями
  const [categories, setCategories] = useState<Category[]>([]);
  const [newCategoryName, setNewCategoryName] = useState<string>('');

  // Функція завантаження користувачів
  const fetchUsers = async () => {
    try {
      const response = await api.get('/api/admin/users');
      setUsers(response.data);
    } catch (error) {
      console.error('Error fetching users:', error);
    }
  };

  // Функція завантаження категорій
  const fetchCategories = async () => {
    try {
      const response = await api.get('/api/admin/categories');
      setCategories(response.data);
    } catch (error) {
      console.error('Error fetching categories:', error);
    }
  };

  // Функція для зміни ролі користувача
  const handleRoleChange = async (userId: number, newRole: string) => {
    try {
      const response = await api.put(`/api/admin/users/${userId}`, { role: newRole });
      console.log('User updated:', response.data);
      fetchUsers();
    } catch (error) {
      console.error('Error updating user:', error);
    }
  };

  // Функція створення нової категорії
  const handleCreateCategory = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    try {
      const response = await api.post('/api/admin/categories', { name: newCategoryName });
      console.log('Category created:', response.data);
      setNewCategoryName('');
      fetchCategories();
    } catch (error) {
      console.error('Error creating category:', error);
    }
  };

  useEffect(() => {
    fetchUsers();
    fetchCategories();
  }, []);

  return (
    <Container className="mt-4">
      <h2>Admin Panel - User Management</h2>
      <Table striped bordered hover responsive className="mt-3">
        <thead>
          <tr>
            <th>ID</th>
            <th>Email</th>
            <th>Role</th>
            <th>Change Role</th>
          </tr>
        </thead>
        <tbody>
          {users.map(user => (
            <tr key={user.id}>
              <td>{user.id}</td>
              <td>{user.email}</td>
              <td>{user.role}</td>
              <td>
                <Button
                  variant="secondary"
                  size="sm"
                  onClick={() =>
                    handleRoleChange(user.id, user.role === 'ROLE_ADMIN' ? 'ROLE_USER' : 'ROLE_ADMIN')
                  }
                >
                  {user.role === 'ROLE_ADMIN' ? 'Set as User' : 'Set as Admin'}
                </Button>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>

      <hr />

      <h2 className="mt-4">Category Management</h2>
      <Form onSubmit={handleCreateCategory} className="mb-3">
        <Row>
          <Col md={8}>
            <Form.Control
              type="text"
              placeholder="Enter new category name"
              value={newCategoryName}
              onChange={(e) => setNewCategoryName(e.target.value)}
              required
            />
          </Col>
          <Col md={4}>
            <Button type="submit" variant="primary" className="w-100">
              Create Category
            </Button>
          </Col>
        </Row>
      </Form>

      <Table striped bordered hover responsive>
        <thead>
          <tr>
            <th>ID</th>
            <th>Category Name</th>
          </tr>
        </thead>
        <tbody>
          {categories.map(cat => (
            <tr key={cat.id}>
              <td>{cat.id}</td>
              <td>{cat.name}</td>
            </tr>
          ))}
        </tbody>
      </Table>
    </Container>
  );
};

export default AdminPanel;
