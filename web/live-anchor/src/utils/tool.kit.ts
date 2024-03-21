export const extractRichText = (str?: string) => {
  return (str || "").replace(/<[^<>]+>/g, "");
};
