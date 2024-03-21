type ValidateResult = {
  msg: string;
  result: boolean;
};
type ValidateFun<T = any> = { validate: (value: T, key: string) => ValidateResult };
export type ValidateRule<T = { [key: string]: any }> = {
  [K in keyof T]?: ValidateFun<T[K]>[];
};

const validate = <T = { [key: string]: any }>(model: T, rules: ValidateRule<T>): ValidateResult => {
  const result = {
    msg: '',
    result: true,
  };
  for (const key in rules) {
    const values = rules[key] as ValidateFun[];
    for (let i = 0; i < values.length; i++) {
      const cur = values[i];
      const subRes = cur.validate(model[key], key);
      result.msg = subRes.msg;
      result.result = subRes.result;
      if (!result.result) {
        break;
      }
    }
    if (!result.result) {
      break;
    }
  }
  return result;
};
const validatePromise = <T = { [key: string]: any }>(model: T, rules: ValidateRule<T>) => {
  return new Promise<ValidateResult>((resolve) => {
    const result = validate<T>(model, rules);
    resolve(result);
  });
};
export const useValidate = () => {
  return { validate, validatePromise };
};
