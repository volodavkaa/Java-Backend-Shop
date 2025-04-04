import React, { useState, useEffect } from 'react';
import { Container, Row, Col, Card, Form } from 'react-bootstrap';
import api from '../api/api';

interface Category {
  id: number;
  name: string;
}

interface ProductPhoto {
  id: number;
  url: string;
}

interface Product {
  id: number;
  title: string;
  description: string;
  price: number;
  category: Category;
  photos: ProductPhoto[];
}

const ProductListPage: React.FC = () => {
  const [products, setProducts] = useState<Product[]>([]);
  const [searchTerm, setSearchTerm] = useState('');

  useEffect(() => {
    const fetchProducts = async () => {
      try {
        const response = await api.get('/products');
        setProducts(response.data);
      } catch (error) {
        console.error('Error fetching products:', error);
      }
    };

    fetchProducts();
  }, []);

  const filteredProducts = products.filter(product =>
    product.title.toLowerCase().includes(searchTerm.toLowerCase()) ||
    product.description.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <Container className="mt-4">
      <h2 className="mb-4">Products</h2>
      <Row className="mb-3">
        <Col md={6}>
          <Form.Control
            type="text"
            placeholder="Search products..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
        </Col>
      </Row>
      <Row>
        {filteredProducts.map(product => (
          <Col key={product.id} md={4} className="mb-3">
            <Card>
              {product.photos && product.photos.length > 0 && (
                <Card.Img variant="top" src={product.photos[0].url} alt={product.title} />
              )}
              <Card.Body>
                <Card.Title>{product.title}</Card.Title>
                <Card.Text>{product.description}</Card.Text>
                <Card.Text>Price: ${product.price}</Card.Text>
                {product.category && (
                  <Card.Text>Category: {product.category.name}</Card.Text>
                )}
              </Card.Body>
            </Card>
          </Col>
        ))}
      </Row>
    </Container>
  );
};

export default ProductListPage;
