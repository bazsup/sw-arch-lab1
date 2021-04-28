# Guideline

## Setup terraform
1. Download [Terraform CLI](https://www.terraform.io/downloads.html)

2. Move terraform binary to `bin` directory (e.g. ~/bin/terraform)

3. Verify terraform can run by using
```bash
terraform -version
```

## Setup Azure CLI
1. Download [Azure CLI](https://docs.microsoft.com/en-us/cli/azure/install-azure-cli)

2. Login
```bash
az login
```

## Create azure resource
1. Initial terraform workspace
```bash
terraform init

```
2. Plan
```bash
make plan
```

3. Apply
```bash
make apply
```


