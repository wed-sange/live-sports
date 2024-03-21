# Sports Live H5

## update file [constant.ts](build%2Fconstant.ts)
```bash
# Install dependencies using yarn
yarn install

# Modify reverse proxy address, open build/constant.ts file, and modify API_TARGET_URL
export const API_TARGET_URL = 'https://******';

# When deploying, the API_TARGET_URL will be automatically read as the online baseUrl

# Local startup command
yarn dev

# Build command
yarn build
```
