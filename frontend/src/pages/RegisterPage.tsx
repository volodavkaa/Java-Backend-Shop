import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { registerUser } from '../api/authApi';
import { Container, Row, Col, Card, Form, Button } from 'react-bootstrap';

const RegisterPage: React.FC = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();

  const handleRegister = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    try {
      const result = await registerUser(email, password);
      console.log('Register result:', result);
      navigate('/login');
    } catch (error) {
      console.error('Register error', error);
    }
  };

  return (
    <Container
      fluid
      className="d-flex align-items-center justify-content-center"
      style={{ minHeight: '100vh', backgroundColor: '#f8f9fa' }}
    >
      <Row className="justify-content-center w-100" style={{ maxWidth: '600px' }}>
        <Col>
          <Card>
            <Card.Body>
              <h2 className="text-center mb-4">Sign Up</h2>
              <Form onSubmit={handleRegister}>
                <Form.Group className="mb-3" controlId="formBasicEmail">
                  <Form.Label>Email address</Form.Label>
                  <Form.Control
                    type="email"
                    placeholder="Enter email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    required
                  />
                </Form.Group>
                <Form.Group className="mb-3" controlId="formBasicPassword">
                  <Form.Label>Password</Form.Label>
                  <Form.Control
                    type="password"
                    placeholder="Password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    required
                  />
                </Form.Group>
                <Button variant="primary" type="submit" className="w-100 mb-2">
                  Register
                </Button>
              </Form>
              <div className="text-center">
                <Button
                  variant="link"
                  onClick={() => navigate('/login')}
                >
                  Already have an account? Login
                </Button>
              </div>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

export default RegisterPage;
