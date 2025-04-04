import React, { useState, useEffect } from 'react';
import { Container, Row, Col, Card, Form, Button } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import api from '../api/api';

interface Category {
  id: number;
  name: string;
}

interface ProductDTO {
  title: string;
  description: string;
  price: number;
  categoryId: number;
  photoUrls: string[];
}

const CreateProductPage: React.FC = () => {
  const navigate = useNavigate();


  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [price, setPrice] = useState<number>(0);
  const [categoryId, setCategoryId] = useState<number | null>(null);
  const [photoUrls, setPhotoUrls] = useState<string[]>(['']);


  const [categories, setCategories] = useState<Category[]>([]);

  useEffect(() => {
    const fetchCategories = async () => {
      try {
        const response = await api.get('/api/categories');
        setCategories(response.data);
        if (response.data.length > 0) {
          setCategoryId(response.data[0].id);
        }
      } catch (error) {
        console.error('Error fetching categories:', error);
      }
    };
    fetchCategories();
  }, []);

  const handlePhotoUrlChange = (index: number, value: string) => {
    const newPhotoUrls = [...photoUrls];
    newPhotoUrls[index] = value;
    setPhotoUrls(newPhotoUrls);
  };

  const addPhotoUrlField = () => {
    setPhotoUrls([...photoUrls, '']);
  };

  const removePhotoUrlField = (index: number) => {
    const newPhotoUrls = photoUrls.filter((_, i) => i !== index);
    setPhotoUrls(newPhotoUrls);
  };

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    try {
      const filteredPhotoUrls = photoUrls.filter(url => url.trim() !== '');
      const productDTO: ProductDTO = {
        title,
        description,
        price,
        categoryId: categoryId!, 
        photoUrls: filteredPhotoUrls,
      };
      const response = await api.post('/products', productDTO);
      console.log('Product created:', response.data);
      navigate('/products');
    } catch (error) {
      console.error('Error creating product:', error);
    }
  };

  return (
    <Container className="mt-4">
      <Row className="justify-content-center">
        <Col md={8}>
          <Card>
            <Card.Body>
              <h2 className="text-center mb-4">Create New Product</h2>
              <Form onSubmit={handleSubmit}>
                <Form.Group className="mb-3" controlId="productTitle">
                  <Form.Label>Title</Form.Label>
                  <Form.Control
                    type="text"
                    placeholder="Enter product title"
                    value={title}
                    onChange={(e) => setTitle(e.target.value)}
                    required
                  />
                </Form.Group>

                <Form.Group className="mb-3" controlId="productDescription">
                  <Form.Label>Description</Form.Label>
                  <Form.Control
                    as="textarea"
                    rows={4}
                    placeholder="Enter product description"
                    value={description}
                    onChange={(e) => setDescription(e.target.value)}
                    required
                  />
                </Form.Group>

                <Form.Group className="mb-3" controlId="productPrice">
                  <Form.Label>Price</Form.Label>
                  <Form.Control
                    type="number"
                    placeholder="Enter product price"
                    value={price}
                    onChange={(e) => setPrice(Number(e.target.value))}
                    required
                  />
                </Form.Group>

                <Form.Group className="mb-3" controlId="productCategory">
                  <Form.Label>Category</Form.Label>
                  <Form.Select
                    value={categoryId ?? ''}
                    onChange={(e) => setCategoryId(Number(e.target.value))}
                    required
                  >
                    {categories.map((cat) => (
                      <option key={cat.id} value={cat.id}>
                        {cat.name}
                      </option>
                    ))}
                  </Form.Select>
                </Form.Group>

                <Form.Group className="mb-3" controlId="productPhotos">
                  <Form.Label>Photo URLs</Form.Label>
                  {photoUrls.map((url, index) => (
                    <div key={index} className="d-flex mb-2">
                      <Form.Control
                        type="text"
                        placeholder="Enter photo URL"
                        value={url}
                        onChange={(e) => handlePhotoUrlChange(index, e.target.value)}
                      />
                      {photoUrls.length > 1 && (
                        <Button
                          variant="danger"
                          onClick={() => removePhotoUrlField(index)}
                          className="ms-2"
                        >
                          Remove
                        </Button>
                      )}
                    </div>
                  ))}
                  <Button variant="secondary" onClick={addPhotoUrlField}>
                    Add Photo URL
                  </Button>
                </Form.Group>

                <Button variant="primary" type="submit" className="w-100">
                  Create Product
                </Button>
              </Form>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

export default CreateProductPage;
