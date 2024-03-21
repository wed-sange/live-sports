# Sports Live Broadcasting Management System

## update file [.env.development](.env.development)
```bash

# live-sports-admin-v1 Sports Live Broadcasting Management Admin Root Directory
# .env.development Testing Environment Configuration 
VUE_APP_BASE_API = 'https://*****/apis/admin'

# .env.production Production Environment Configuration 
VUE_APP_BASE_API = 'https://*****/apis/admin'

# Install dependencies
npm install

# It is recommended not to use cnpm directly to install dependencies, as there may be various strange bugs. 
# You can solve the problem of slow npm download speed by the following operation
npm install --registry=https://registry.npmmirror.com

# Start the service
npm run dev
```

Access http://localhost:80 in your browser.

## Release

```bash
# Build for staging environment
npm run build:stage

# Build for production environment
npm run build:prod

```