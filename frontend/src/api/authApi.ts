import api from './api' 

export async function registerUser(email: string, password: string) {
  const response = await api.post('/api/auth/register', {
    email,
    password,
  })
  return response.data
}

export async function loginUser(email: string, password: string) {
  const response = await api.post('/api/auth/login', {
    email,
    password,
  })
  return response.data
}
