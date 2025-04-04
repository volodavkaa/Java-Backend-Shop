import React, { useState, useEffect } from 'react';
import { Container, Row, Col, Card, Form, Button, Table } from 'react-bootstrap';
import api from '../api/api';

interface Category {
  id: number;
  name: string;
}

const CategoryPage: React.FC = () => {
  const [categories, setCategories] = useState<Category[]>([]);
  const [name, setName] = useState('');

  const fetchCategories = async () => {
    try {
      const response = await api.get('/api/categories');
      setCategories(response.data);
    } catch (error) {
      console.error('Error fetching categories:', error);
    }
  };

  useEffect(() => {
    fetchCategories();
  }, []);

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    try {
      const response = await api.post('/api/categories', { name });
      console.log('Category created:', response.data);
      setName('');
      fetchCategories(); 
    } catch (error) {
      console.error('Error creating category:', error);
    }
  };

  return (
    <Container className="mt-4">
      <Row className="mb-4">
        <Col>
          <h2>Manage Categories</h2>
        </Col>
      </Row>
      <Row className="mb-4">
        <Col md={6}>
          <Card>
            <Card.Body>
              <h4>Add New Category</h4>
              <Form onSubmit={handleSubmit}>
                <Form.Group className="mb-3" controlId="formCategoryName">
                  <Form.Label>Category Name</Form.Label>
                  <Form.Control
                    type="text"
                    placeholder="Enter category name"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    required
                  />
                </Form.Group>
                <Button variant="primary" type="submit" className="w-100">
                  Create Category
                </Button>
              </Form>
            </Card.Body>
          </Card>
        </Col>
      </Row>
      <Row>
        <Col>
          <h4>Categories List</h4>
          <Table striped bordered hover responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Name</th>
              </tr>
            </thead>
            <tbody>
              {categories.map((cat) => (
                <tr key={cat.id}>
                  <td>{cat.id}</td>
                  <td>{cat.name}</td>
                </tr>
              ))}
            </tbody>
          </Table>
        </Col>
      </Row>
    </Container>
  );
};

export default CategoryPage;
